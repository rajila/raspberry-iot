% Information about 'Input channel. JSON Planta' CHannel
% Channel ID 
ChannelIDInputJSON = 667670;
% Channel Read API Key 
readAPIKeyInputJSON = 'YSBXCABAK2L88NM3';

%%%% Information about 'THL' Channel
%%%% Channel ID
%%%%ChannelIDTHL = 639107;
%%%% Channel Read API Key 
%%%%readAPIKeyTHL = 'IKH4TCFWRIT3GG2G'
%%%% Channel Write API Key
%%%%writeAPIKeyTHL = 'LZSMN2D2DNV88ANH';

%%%% Information about 'Canal PC' Channel
%%%% Channel ID 
%%%%ChannelIDPC = 639108;
%%%% Channel Read API Key
%%%%readAPIKeyPC = '70TLR15YZYKI4HLV'
%%%% Channel Write API Key   
%%%%writeAPIKeyPC='IA2CCD97ZJQEVBUF'

%%%% Talkback Identification
%%%% TalkBack app ID
%%%%TalkBack_ID = '29518';
%%%% TalkBack app API key
%%%%TalkBack_apikey = 'ARLVXFW6CHWBAH42';


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

if(data(1,2) < 0)
    amountFoodCurrent = 0;
else
    amountFoodCurrent = data(1,2);
end
milliliterWaterCurrent = data(1,1);

% Consulta de Horarios de alimentación AZURE
urlTB = strcat('https://dispenserservice.azurewebsites.net/api/FoodHours');
optionsTB = weboptions('RequestMethod','GET','MediaType','application/json');
valFoodH = webread(urlTB,optionsTB);

pause (1);

timeCurrentL = strsplit(datestr(now,'HH:MM:SS'),':');
dataSecondsCurrent = zeros(1,3);
dataSecondsCurrent(1,1) = str2double(timeCurrentL(1))*60*60;
dataSecondsCurrent(1,2) = str2double(timeCurrentL(2))*60;
dataSecondsCurrent(1,3) = str2double(timeCurrentL(3));
totalSegCurrent = sum(dataSecondsCurrent);

dispenserFood = 1; % Default a 0
dataSecondsDB = zeros(1,3);
for m = 1:length(valFoodH)
    hourDB = valFoodH(m).Hour;
    hourDBL = strsplit(hourDB,'T');
    timeCurrentDBL = strsplit(datestr(hourDBL(2),'HH:MM:SS'),':');
    dataSecondsDB(1,1) = str2double(timeCurrentDBL(1))*60*60;
    dataSecondsDB(1,2) = str2double(timeCurrentDBL(2))*60;
    dataSecondsDB(1,3) = str2double(timeCurrentDBL(3));
    totalSegDB = sum(dataSecondsDB);
    diffSeg = totalSegCurrent - totalSegDB;
    if( diffSeg >=0 && diffSeg < 25 )
        dispenserFood = 1;
        break;
    end
end

% Consulta de Horarios de alimentación AZURE
urlTB = strcat('https://dispenserservice.azurewebsites.net/api/Configurations');
optionsTB = weboptions('RequestMethod','GET','MediaType','application/json');
valConfigurationDB = webread(urlTB,optionsTB);
    
pause (1);

amountDailyFoodDB = valConfigurationDB(1).AmountDailyFood;
foodPortion = amountDailyFoodDB/length(valFoodH);
milliLiterWater = valConfigurationDB(1).MilliLiterWater;
minPercentWater = valConfigurationDB(1).MinPercentWater;
milliLiterMin = (milliLiterWater*minPercentWater)/100;



%if( dispenserFood == 1 )
%else 
%end