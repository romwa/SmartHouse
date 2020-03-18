//code for main arduino
#include "nrf_24l01.h";

byte selfAddr[5] = {0x00,0x00,0x00,0x00,0x01};
byte lightAddr[5] = {0x00,0x00,0x00,0x00,0x02};
byte lockAddr[5] = {0x00,0x00,0x00,0x00,0x03};
byte addr[5] = {0xDD,0xDD,0xDD,0xDD,0xEE};
char datout[] = {0, 0};
char datin[] = {0,0,0};
byte pipe=0;

char CHECK = 1;
char LIGHT_ON='n';
char LIGHT_OFF='f';
char LOCK='l';
char UNLOCK='u';
char CONFIRM='c';
char IDLE_STATE = 'd';
char AWAITING_CONFIRMATION = 'a';
char ERROR_MESSAGE = 'e';
char command = IDLE_STATE;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  RF.begin();
  RF.setTXaddr(1,addr);
  RF.setRXaddr(1,addr);
  RF.setTX();
  delay(100);

  printInfo();
  NRF_print_address();
}

void loop() {
  // put your main code here, to run repeatedly:

  command = recieveComp();
  command = 'n';
  if(command != IDLE_STATE && command != AWAITING_CONFIRMATION && command != 0) setDatout(CHECK, command);
  if(datout[0] != 0) {
    Serial.println("need to send " + convertToString(datout,2));
    if(command == LIGHT_ON || command == LIGHT_OFF) sendMessage(lightAddr);
    else if(command == LOCK || command == UNLOCK) sendMessage(lockAddr);
  } else if(command == AWAITING_CONFIRMATION) {
    recieve();
    if(datin[0] == 1) {
      Serial.print(datin[3]);  
    }
  }
  
}

//sends datout to addr[]
//only if have what to send
void sendMessage(byte addr[]) {
  RF.setTX();
  if(datout[0] == 1) {
//   Serial.println("real Data");
   byte bufferout;
   for(int i = 0; i < 3; i++) {
     bufferout = datout[i];
     RF.send(&bufferout);
   }
//   Serial.println("done Sending");
   setDatout(0,0);
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
}



//sets datout
void setDatout(char check, char lastAction) {
//  datout[] = {check, lastAction, confirm};
  datout[0] = check;
  datout[1] = lastAction;
}

char recieveComp() {
  return Serial.readString().charAt(0);
}

void sendComp(char dat[]) {
  Serial.print(convertToString(dat, 3));
}

String convertToString(char a[], int aSize) {
  String s = "";
  for(int i = 0; i < aSize; i++) {
    s = s + a[i];
  }
  return s;
}



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
