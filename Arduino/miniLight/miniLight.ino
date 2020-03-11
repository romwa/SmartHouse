//code for the arduino mini that acontrols the light
#include "nrf_24101.h"

//address of reciever/transmitter
byte addr[5] = {0xF6,0xF6,0xF6,0xF6,0x01);

//data to be sent if lighting was successful
char LIGHT_CONFIRMATION= 1;
char LIGHT_ERROR = 0;
char lightMessage = -1;

//data that comes in (-1 if no data came in)
char datIn = -1;
char LIGHT_ON = 1;
char LIGHT_OFF = 0;

//1 for transmitter 0 for reciever
byte currentState;

//1 if ready to read,0 if not
byte pipe=0;

void setup() {
  pinMode(A0,OUTPUT);//set pin for LED
  
  pinMode(A2, INPUT_PULLUP); //need to ask what it is used for
  currentState = 0; //reciever for start
  Serial.begin(9600); //need to ask what for

  RF.begin(); //default is channel 2
//  RF.setChannel(18); //to change channel

  //set address for reciever and transmitter
  RF.setTXaddr(1,addr); //shouldnt this be the address that im sending it to>?
  RF.setRXaddr(1,addr);

  //decide if T or R
  if(currentState) RF.setTX();
  else RF.setRX();

  //need 1.5 ms to reach standby mode (CE=low)
  delay(100);

  printInfo();
  NRF_print_address();
}

void loop() {
  // put your main code here, to run repeatedly:

  if(currentState && lightMessage!=-1) {//if transmittor && there is something to send
    RF.send(&lightMessage);//ask what & is
    delay(50);
    lightMessage=-1;
    RF.setRX();
  } else {//if reciever
    if(RF.readReady(&pipe)) {
      RF.read(&datIn);
      if(datIn == LIGHT_ON) {
        lightOn();
        datIn=-1;
        RF.setTX();
      } else if(datIn == LIGHT_OFF){
        lightOff();
        datIn=-1;
        RF.setTX();
      }
    }
  }
}

void lightOn() {
  digitalWrite(A0, HIGH);
  delay(100);
  lightMessage = LIGHT_CONFIRMATION;
}

void lightOff() {
  digitalWrite(A0,LOW);
  delay(100);
  lightMessage = LIGHT_CONFIRMATION;
}

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
