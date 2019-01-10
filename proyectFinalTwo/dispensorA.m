% Information about 'Input channel. JSON Planta' CHannel
% Channel ID 
ChannelIDInputJSON = 667670;
% Channel Read API Key 
readAPIKeyInputJSON = 'YSBXCABAK2L88NM3';

% Information about 'Water & Food Dispenser' Channel
% Channel ID 
ChannelIDWFD = 668761;
% Channel Read API Key
readAPIKeyWFD = '1PM6BPHGRM8HNSJZ';
% Channel Write API Key   
writeAPIKeyWFD ='JD0IUXEYPMYX2I1S';

% Information about 'LOGs Water & Food Dispenser' Channel
% Channel ID 
ChannelIDLWFD = 673860;
% Channel Read API Key
readAPIKeyLWFD = '4CNCR4TV8CJ8UE14';
% Channel Write API Key   
writeAPIKeyLWFD ='768H44QS2AJ8EFVC';

% Talkback Identification
% TalkBack app ID
TalkBack_ID = '30174';
% TalkBack app API key
TalkBack_apikey = '82VRSK83J7RYH5OF';

% Parametros por defecto
AmountDailyFoodINI = 500; % gramos 
AmountBowlFoodWaterINI = 250;
AmountDailyWaterINI = 2000; % mililitros


% Reading the JSON in field1 'Input channel. JSON Planta' Channel 
stringJSON = thingSpeakRead(ChannelIDInputJSON, 'Field', [1], 'NumPoints', 1,'outputFormat', 'table', 'Readkey', readAPIKeyInputJSON);

% Uncomment to visualize
%stringJSON.fieldJSON{1}

% jsondecode maps the JSON in an equivalent structure
valInputJSON = jsondecode (stringJSON.fieldJSON{1});

% Uncomment to visualize
%hora=valInputJSON.dispenser.obs.time

% array to store Water Level in position 1, Digital Balance in 2
% In this way, the order of arrival of Water Level, Digital Balance does not matter
data = zeros(1,2);

% Getting idThing
%idThing = str2num(valInputJSON.dispenser.idThing);
idThing = valInputJSON.dispenser.idThing;

for m=1:2
    % Getting and processing Water Level, Digital Balance.
    % They can be in any order
    type= valInputJSON.dispenser.obs.sensor(m).obProp;
    if strcmp(type,'int')
        if (isnumeric (valInputJSON.dispenser.obs.sensor(m).out))
            value = round (valInputJSON.dispenser.obs.sensor(m).out,0);
        else
            value = round (str2num(valInputJSON.dispenser.obs.sensor(m).out),0);
        end
    elseif strcmp(type,'dbl')
        if (isnumeric (valInputJSON.dispenser.obs.sensor(m).out))
            value = valInputJSON.dispenser.obs.sensor(m).out;
        else
            value = str2double(valInputJSON.dispenser.obs.sensor(m).out);
        end
    end
       
    if (strcmp (valInputJSON.dispenser.obs.sensor(m).id,'wtl'))
        data(1,1) = value;
    elseif (strcmp (valInputJSON.dispenser.obs.sensor(m).id,'dgb'))
        data(1,2) = value;
    end
end %del for

% Consulta de Datos del Dispensador
urlTB = strcat('https://dispenserservice.azurewebsites.net/api/ConfigurationParameters');
optionsTB = weboptions('RequestMethod','GET','MediaType','application/json');
valDispenserJSON = webread(urlTB,optionsTB);

pause (1);

if( length(valDispenserJSON) ~= 0 )
    AmountDailyFoodINI = valDispenserJSON(1).AmountDailyFood;
    AmountDailyWaterINI = valDispenserJSON(1).AmountDailyWater;
    AmountBowlFoodWaterINI= valDispenserJSON(1).AmountBowlFoodWater;
end

% LIMPIAR DATOS ENTRADA (WATER)
if(data(1,1) < 0)
    amountWaterCurrent = 0;
else
    if(data(1,1) > AmountBowlFoodWaterINI)
        amountWaterCurrent = AmountBowlFoodWaterINI;
    else
        amountWaterCurrent = data(1,1);
    end
end

% LIMPIAR DATOS ENTRADA (FOOD)
if(data(1,2) < 0)
    amountFoodCurrent = 0;
else
    if(data(1,2) > AmountBowlFoodWaterINI)
        amountFoodCurrent = AmountBowlFoodWaterINI;
    else
        amountFoodCurrent = data(1,2);
    end
end

% Lectura de los registros del día, para sacar una suma de la cantidad dispensada
dataLWFD = thingSpeakRead(ChannelIDLWFD,'Fields',[1,2,3,4,5],'OutputFormat','table','ReadKey', readAPIKeyLWFD, 'DateRange',[datetime(datestr(now,'dd-mmm-yyyy')),datetime(datestr(datetime('tomorrow'),'dd-mmm-yyyy'))]);

pause(1);

%datetime(datestr(now,'dd-mmm-yyyy'))
%datetime(datestr(datetime('tomorrow'),'dd-mmm-yyyy'))
%datetime('now','InputFormat','dd-mmm-yyyy')

if( height(dataLWFD) == 0)
    amountFoodAvailable = AmountDailyFoodINI;
    amountWaterAvailable = AmountDailyWaterINI;
else
    amountFoodAvailable = AmountDailyFoodINI - sum(dataLWFD.amountFoodDownloaded);
    amountWaterAvailable = AmountDailyWaterINI - sum(dataLWFD.amountWaterDownloaded);
end

diffFood = AmountBowlFoodWaterINI - amountFoodCurrent;
diffWater = AmountBowlFoodWaterINI - amountWaterCurrent;

dispenseFood = 0;
if( amountFoodAvailable <= 0 )
    diffFood = 0;
else
    dispenseFood = 1;
    if( diffFood > amountFoodAvailable )
        diffFood = amountFoodAvailable;
    end
end

dispenseWater = 0;
if( amountWaterAvailable <= 0 )
    diffWater = 0;
else
    dispenseWater = 1;
    if( diffWater > amountWaterAvailable )
        diffWater = amountWaterAvailable;
    end
end

if(dispenseFood == 1 && dispenseWater == 1)
    dispenseType = 3; % Dispensa Food & Water
elseif(dispenseFood == 0 && dispenseWater == 0)
    dispenseType = 0; % No Dispensa Food Ni Water
elseif(dispenseFood == 1 && dispenseWater == 0)
    dispenseType = 1; % Dispensa Food
else
    dispenseType = 2; % Dispensa Water
end

if(dispenseType == 1 || dispenseType == 2 || dispenseType == 3)
    % Sending data values to 'LOGs Water & Food Dispenser' Channel
    thingSpeakWrite(ChannelIDLWFD, {amountFoodCurrent, diffFood, amountWaterCurrent, diffWater, AmountBowlFoodWaterINI}, 'WriteKey', writeAPIKeyLWFD);
    pause (1) % stop the execution 1 second to let the writing in 'LOGs Water & Food Dispenser' Channel
end

% Reglas para dispensarn Alimentos
angleServoFood = 0;
openingSecondsFood = 5;
if( diffFood <= 0 )
    angleServoFood = 0;
    openingSecondsFood = 0;
elseif(diffFood <= (AmountBowlFoodWaterINI*0.10))
    angleServoFood = 10;
elseif(diffFood <= (AmountBowlFoodWaterINI*0.20))
    angleServoFood = 20;
elseif(diffFood <= (AmountBowlFoodWaterINI*0.30))
    angleServoFood = 30;
elseif(diffFood <= (AmountBowlFoodWaterINI*0.40))
    angleServoFood = 40;
elseif(diffFood <= (AmountBowlFoodWaterINI*0.50))
    angleServoFood = 50;
elseif(diffFood <= (AmountBowlFoodWaterINI*0.60))
    angleServoFood = 60;
elseif(diffFood <= (AmountBowlFoodWaterINI*0.70))
    angleServoFood = 70;
elseif(diffFood <= (AmountBowlFoodWaterINI*0.80))
    angleServoFood = 80;
elseif(diffFood <= (AmountBowlFoodWaterINI*0.90))
    angleServoFood = 90;
else
    angleServoFood = 120;
end

% Reglas para dispensar Agua
angleServoWater = 0;
openingSecondsWater = 5;
if (diffWater <= 0)
    angleServoWater = 0;
    openingSecondsWater = 0;
elseif(diffWater <= (AmountBowlFoodWaterINI*0.10))
    angleServoWater = 10;
elseif(diffWater <= (AmountBowlFoodWaterINI*0.20))
    angleServoWater = 20;
elseif(diffWater <= (AmountBowlFoodWaterINI*0.30))
    angleServoWater = 30;
elseif(diffWater <= (AmountBowlFoodWaterINI*0.40))
    angleServoWater = 40;
elseif(diffWater <= (AmountBowlFoodWaterINI*0.50))
    angleServoWater = 50;
elseif(diffWater <= (AmountBowlFoodWaterINI*0.60))
    angleServoWater = 60;
elseif(diffWater <= (AmountBowlFoodWaterINI*0.70))
    angleServoWater = 70;
elseif(diffWater <= (AmountBowlFoodWaterINI*0.80))
    angleServoWater = 80;
elseif(diffWater <= (AmountBowlFoodWaterINI*0.90))
    angleServoWater = 90;
else
    angleServoWater = 120;
end

%dateString = datestr(now,'yyyy-mm-dd HH:MM:SS');

% Inserta Log en REST API dispensorService AZURE
urlLogs = strcat('https://dispenserservice.azurewebsites.net/api/Logs');
% Building the string whit the JSON and sending to REST API dispensorService Logs
bodyLogsJSON = strcat('{"ThingId": "',idThing,'","ActionType": "AUTOMATIC","AmountDailyFood": ',num2str(AmountDailyFoodINI),',"AmountBowlFood": ',num2str(AmountBowlFoodWaterINI),',"AmountDailyWater": ',num2str(AmountDailyWaterINI),',"AmountBowlWater": ',num2str(AmountBowlFoodWaterINI),',"CurrentAmountFood": ',num2str(amountFoodCurrent),',"AmountFoodDownloaded": ',num2str(diffFood),',"CurrentAmountWater": ',num2str(amountWaterCurrent),',"AmountWaterDownloaded": ',num2str(diffWater),',"AngleServoFood": ',num2str(angleServoFood),',"OpeningSecondsFood": ',num2str(openingSecondsFood),',"AngleServoWater": ',num2str(angleServoWater),',"OpeningSecondsWater": ',num2str(openingSecondsWater),',"DispenseFood": ',num2str(dispenseFood),',"DispenseWater": ',num2str(dispenseWater),'}');
optionsLogs = weboptions('ContentType','json','ArrayFormat','json','RequestMethod','post','MediaType','application/json');
responseLogsJSON = webwrite( urlLogs, bodyLogsJSON, optionsLogs);

pause (1);

% Sending data values to 'Water & Food Dispenser' Channel
thingSpeakWrite(ChannelIDWFD, {idThing, diffFood, angleServoFood, openingSecondsFood, diffWater, angleServoWater, openingSecondsWater, dispenseType}, 'WriteKey', writeAPIKeyWFD);

pause (1) % stop the execution 1 second to let the writing in 'Water & Food Dispenser' Channel

% Reading the las value of 1, 2 y 3 de Price Confort Channel 
dataWFD = thingSpeakRead(ChannelIDWFD,'Fields',[1,2,3,4,5,6,7,8],'NumPoints',1,'OutputFormat','table','ReadKey', readAPIKeyWFD);

if(dataWFD.DispenseType == 1 || dataWFD.DispenseType == 2 || dataWFD.DispenseType == 3 )
    if(dataWFD.DispenseType == 1)
        angleFood = dataWFD.AngleServoFood;
        secondsFood = dataWFD.OpeningSecondsFood;
        angleWater = 0;
        secondsWater = 0;
    elseif(dataWFD.DispenseType == 2)
        angleFood = 0;
        secondsFood = 0;
        angleWater = dataWFD.AngleServoWater;
        secondsWater = dataWFD.OpeningSecondsWater;
    else
        angleFood = dataWFD.AngleServoFood;
        secondsFood = dataWFD.OpeningSecondsFood;
        angleWater = dataWFD.AngleServoWater;
        secondsWater = dataWFD.OpeningSecondsWater;
    end

    % 001 -> Servo Food
    % 002 -> Servo Water
    % Composing the JSON that it will be sent to the Talkback
    %{  
    %   "dispenser":{  
    %      "idThing":"179",
    %      "act":[  
    %         {
    %            "id": "001",
    %            "type": "rotate",
    %            "dsc": 
    %             {
    %                 "ang": 0,
    %                 "sec": 0
    %             }
    %         },
    %         {  
    %            "id":"002",
    %            "type":"rotate",
    %            "dsc": 
    %             {
    %                 "ang": 80,
    %                 "sec": 5
    %             }
    %         }
    %      ]
    %   }
    %}

    % Building the string whit the JSON and sending to Talkback
    jsonTalkBackOUT = strcat('{"dispenser":{"idThing":"179","act":[{"id":"001", "type":"rotate", "dsc":{"ang":',num2str(angleFood),',"sec":',num2str(secondsFood),'}},{"id":"002", "type":"rotate", "dsc":{"ang":',num2str(angleWater),',"sec":',num2str(secondsWater),'}}]}}');

    urlTalkBack = strcat('https://api.thingspeak.com/talkbacks/',TalkBack_ID,'/commands');
    dataTalkBack = struct('api_key',TalkBack_apikey,'command_string',jsonTalkBackOUT);
    optionsTalkBack = weboptions('MediaType','application/json');
    response = webwrite(urlTalkBack,dataTalkBack,optionsTalkBack);
end