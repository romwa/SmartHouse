//code for the arduino mini that acontrols the lock
#include "nrf_24101.h"

//address of reciever/transmitter
byte addr[5] = {0xF6,0xF6,0xF6,0xF6,0x02);

//data to be sent if lighting was successful
char LOCK_CONFIRMATION= 1;
char LOCK_ERROR = 0;
char lockMessage = -1;

//data that comes in (-1 if no data came in)
char datIn = -1;
char LOCK = 1;
char UNLOCK = 0;

//1 for transmitter 0 for reciever
byte currentState;

//1 if ready to read,0 if not
byte pipe=0;

//pins for lock
int pin1 = 7;
int pin2 = 8;

//pins rotarion
byte rotation = 0;

void setup() {
  //set pins for lock
  pinMode(pin1, OUTPUT);
  pinMode(pin2, OUTPUT);
  
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
    RF.send(&lockMessage);//ask what & is
    delay(50);
    lockMessage=-1;
    RF.setRX();
  } else {//if reciever
    if(RF.readReady(&pipe)) {
      RF.read(&datIn);
      if(datIn == LOCK) {
        lock();
        datIn=-1;
        RF.setTX();
      } else if(datIn == UNLOCK){
        unlock();
        datIn=-1;
        RF.setTX();
      }
    }
  }
}

void lock() {
  //code for lock
  digitalWrite(pin1,LOW);
  digitalWrite(pin2,LOW);
  lockMessage = LOCK_CONFIRMATION;
}

void unlock() {
  //code for unlock
  if(rotarion) {
    digitalWrite(pin1,HIGH);
    digitalWrite(pin2,LOW);
  } else {
    digitalWrite(pin1,LOW);
    digitalWrite(pin2,HIGH);
  }
  lockMessage = LOCK_CONFIRMATION;
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
