/* Programa para Calibração do HX711
  Blog Eletrogate - http://blog.eletrogate.com/balanca-digital-com-arduino-aprenda-a-usar-a-celula-de-carga
  Arduino UNO - IDE 1.8.5 - Modulo HX711 - celulas de Carga 50 Kg
  Gustavo Murta   31/maio/2018
  Biblioteca https://github.com/bogde/HX711
  Baseado em https://www.hackster.io/MOHAN_CHANDALURU/hx711-load-cell-amplifier-interface-with-arduino-fa47f3
*/

#include "HX711.h"


#define DOUT  26                      // HX711 DATA OUT = pino A0 do Arduino 
#define CLK  27                       // HX711 SCK IN = pino A1 do Arduino 
 
HX711 balanca(DOUT, CLK);             // instancia Balança HX711
 
float calibration_factor = 398030;     // Factor de calibración a 0.200 kg
 
void setup()
{
  Serial.begin(115200);                                              // monitor serial 9600 Bps
  Serial.println();                                                // salta uma linha
  Serial.println("HX711 - Calibracao da Balanca");                 // imprime no monitor serial
  Serial.println("Remova o peso da balanca");
  Serial.println("Depois que as leituras começarem, coloque um peso conhecido sobre a Balança");
  Serial.println("Pressione a,s,d,f para aumentar Fator de Calibracion por 10,100,1000,10000 respectivamente");
  Serial.println("Pressione z,x,c,v para diminuir Fator de Calibracion por 10,100,1000,10000 respectivamente");
  Serial.println("Após leitura correta do peso, pressione t para TARA(zerar) ");
 
  balanca.set_scale();                                             // configura a escala da Balança
  zeraBalanca ();                                                  // zera a Balança
}
 
void zeraBalanca ()
{
  Serial.println();                                               // salta uma linha
  balanca.tare();                                                 // zera a Balança
  Serial.println("Balança Zerada ");
}
 
void loop()
{
  balanca.set_scale(calibration_factor);                     // ajusta fator de calibração
  Serial.print("Peso: ");                                    // imprime no monitor serial
  Serial.print(balanca.get_units(), 3);                      // imprime peso da balança com 3 casas decimais
  Serial.print(" kg");
  Serial.print("      Fator de Calibracion: ");               // imprime no monitor serial
  Serial.println(calibration_factor);                        // imprime fator de calibração
  delay(500) ;                                               // atraso de 0,5 segundo
 
  if (Serial.available())                                    // reconhece letra para ajuste do fator de calibração
  {
    char temp = Serial.read();
    if (temp == '+' || temp == 'a')                // a = aumenta 10
      calibration_factor += 10;
    else if (temp == '-' || temp == 'z')           // z = diminui 10
      calibration_factor -= 10;
    else if (temp == 's')                          // s = aumenta 100
      calibration_factor += 100;
    else if (temp == 'x')                          // x = diminui 100
      calibration_factor -= 100;
    else if (temp == 'd')                          // d = aumenta 1000
      calibration_factor += 1000;
    else if (temp == 'c')                          // c = diminui 1000
      calibration_factor -= 1000;
    else if (temp == 'f')                          // f = aumenta 10000
      calibration_factor += 10000;
    else if (temp == 'v')                          // v = dimuni 10000
      calibration_factor -= 10000;
    else if (temp == 't') zeraBalanca ();          // t = zera a Balança
  }
}
