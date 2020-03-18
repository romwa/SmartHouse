//code for the arduino mini that acontrols the light
#include "nrf_24l01.h";

//address of reciever/transmitter
byte selfAddr[] = {0x00,0x00,0x00,0x00,0x02};
byte mainAddr[] = {0x00,0x00,0x00,0x00,0x01};
byte addr[5] = {0xDD,0xDD,0xDD,0xDD,0xEE};

char datout[] = {0,0,0};
char datin[] = {0,0};


//data to be sent if lighting was successful
char CHECK = '0';//needs to send 1. 0 if nothing to send
char CONFIRM= 'c';
char ERROR_MESSAGE = 'e';

//data that comes in (-1 if no data came in)
//char datIn = -1;
char LIGHT_ON = 'n';
char LIGHT_OFF = 'f';

//1 for transmitter 0 for reciever
//byte currentState;

//1 if ready to read,0 if not
byte pipe=0;

int led = 4;

void setup() {
  pinMode(led,OUTPUT);//set pin for LED
  
//  pinMode(A2, INPUT_PULLUP); //need to ask what it is used for
//  currentState = 0; //reciever for start
  Serial.begin(9600); //need to ask what for

  RF.begin(); //default is channel 2
//  RF.setChannel(18); //to change channel

  //set address for reciever and transmitter
  RF.setTXaddr(1,addr); //shouldnt this be the address that im sending it to>?
  RF.setRXaddr(1,addr);

  //decide if T or R
//  if(currentState) RF.setTX();
//  else RF.setRX();

  //need 1.5 ms to reach standby mode (CE=low)
  delay(100);

  printInfo();
  NRF_print_address();
}

byte i,j;
void loop() {
  if(datout[0] == 1) { //have something to send
    sendMessage(mainAddr);
  } else {
    recieve();
    if(datin[0] == 1) { //contains real data
      if(datin[1] == LIGHT_ON) lightOn();
      else if(datin[1] == LIGHT_OFF) lightOff(); 
    }
  }

  
//  byte bufferin, bufferout;
//
//  if(currentState && lightMessage[0] != 0) {//if transmittor && there is something to send
//    bufferout = lightMessage;
//    RF.send(&bufferout);//ask what & is
//    if(lightMessage[i]) i++;
//      else {
//        delay(50);
//      }
//    delay(50);
//    lightMessage={0,0,0};
//    RF.setRX();
//  } else {//if reciever
//    if(RF.readReady(&pipe)) {
//      RF.read(&bufferin);
//      if(datin[j]) j++;
//          else {
//            j-0;
//            for (i=0;dat[i];i++)
//            Serial.write (dat[i]);
//            Serial.println("");
//            delay(50);
//          }
//      if(datIn == LIGHT_ON) {
//        lightOn();
//        datIn=-1;
//        RF.setTX();
//      } else if(datIn == LIGHT_OFF){
//        lightOff();
//        datIn=-1;
//        RF.setTX();
//      }
//    }
//  }
}

//turns light on
//changes datout to confirm message
void lightOn() {
  digitalWrite(led, HIGH);
  delay(100);
  setDatout(1, LIGHT_ON, CONFIRM);
}

//turns light off
//changes datout to confirm message
void lightOff() {
  digitalWrite(led, LOW);
  delay(100);
  setDatout(1, LIGHT_OFF, CONFIRM);
}

//sets datout
void setDatout(char check, char lastAction, char confirm) {
//  datout[] = {check, lastAction, confirm};
  datout[0] = check;
  datout[1] = lastAction;
  datout[2] = confirm;
}

//sends datout to addr[]
//only if have what to send
void sendMessage(byte addr[]) {
  RF.setTX();
  if(datout[0] == 1) {
   byte bufferout;
   for(int i = 0; i < 3; i++) {
     bufferout = datout[i];
     RF.send(&bufferout);
   }
   setDatout(0,0,0);
  }
}

//recieves data
void recieve() {
  RF.setRX();
  int i = 0;
  while(RF.readReady(&pipe)) {
    byte bufferin;
    RF.read(&bufferin);
    datin[i] = bufferin;
    i++;
  }
  Serial.println(datin[1]);
}

//**************************************************************************

//void lightOn() {
//  digitalWrite(A0, HIGH);
//  delay(100);
////  lightMessage = LIGHT_CONFIRMATION;
//  setLightMessage(CHECK, LIGHT_ON, LIGHT_CONFIRMATION);
//}
//
//void lightOff() {
//  digitalWrite(A0,LOW);
//  delay(100);
////  lightMessage = LIGHT_CONFIRMATION;
//  setLightMessage(CHECK, LIGHT_OFF, LIGHT_CONFIRMATION);
//}
//
//void setLightMessage(char check, char action, char confirm) {
//  lightMessage[0] = check;
//  lightMessage[1] = action;
//  lightMessage[2] = confirm;
//}


















//*************************************************************************************

// print info
void printInfo(){
  print("Config:      ",RF.readRegister(CONFIG));
  print("EN_AA:       ",RF.readRegister(EN_AA));
  print("EN_RXADDR:   ",RF.readRegister(EN_RXADDR));
  print("SETUP_AW:    ",RF.readRegister(SETUP_AW));
  print("SETUP_RETR:  ",RF.readRegister(SETUP_RETR));
  print("RF_CH:       ",RF.readRegister(RF_CH));
  print("RF_SETUP:    ",RF.readRegister(RF_SETUP));
  print("STATUS:      ",RF.readRegister(STATUS));
  print("FIFO STATUS: ",RF.readRegister(FIFO_STATUS));
  print("RX_RW_P0:    ",RF.readRegister(RX_PW_P0));
  print("RX_RW_P1:    ",RF.readRegister(RX_PW_P1));
  print("RX_RW_P2:    ",RF.readRegister(RX_PW_P2));
  print("RX_RW_P3:    ",RF.readRegister(RX_PW_P3));
  print("RX_RW_P4:    ",RF.readRegister(RX_PW_P4));
  print("RX_RW_P5:    ",RF.readRegister(RX_PW_P5));
}

void print(char *st, byte reg){
  Serial.print (st);
  Serial.print (reg,HEX);
  Serial.println("");
}

//print info about address
void NRF_print_address(){
  uint8_t buff[5],i;
  RF.readRegisters(RX_ADDR_P0,buff,5);
  Serial.print("RX_P0:  ");
  for (i=0;i<5;i++){
    Serial.print (buff[i],HEX);
    Serial.print ("  ");
  }
  Serial.println ("");
 
  RF.readRegisters(RX_ADDR_P1,buff,5);
  Serial.print("RX_P1:  ");
  for (i=0;i<5;i++){
    Serial.print (buff[i],HEX);
    Serial.print ("  ");
  }
  Serial.println ("");

  RF.readRegisters(RX_ADDR_P2,buff,5);
  Serial.print("RX_P2:  ");
  for (i=0;i<5;i++){
    Serial.print (buff[i],HEX);
    Serial.print ("  ");
  }
  Serial.println ("");
  

  RF.readRegisters(TX_ADDR,buff,5);

  Serial.print("TX:  ");
  for (i=0;i<5;i++){
    Serial.print (buff[i],HEX);
    Serial.print ("  ");
  }
  Serial.println ("");
}
