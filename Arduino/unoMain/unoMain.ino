//code for the main arduino
#include "nrf_24l01.h";

//things for radio comunication
byte addr[5] = {0x00,0x00,0x00,0x00,0x01};
byte lightAddr[5] = {0x00,0x00,0x00,0x00,0x02};
byte lockAddr[5] = {0x00,0x00,0x00,0x00,0x03};
char dat[] = {0, 'd', 'd'};
char datin[] = {0,'d','d'};
//transmitter / reciever
byte currentState;
byte pipe=0;

char CHECK = '0';
char LIGHT_ON='n';
char LIGHT_OFF='f';
char LOCK='l';
char UNLOCK='u';
char CONFIRM='c';
char IDLE_STATE = 'd';
char ERROR_MESSAGE = 'e';
char command = IDLE_STATE;

//things until miniLight works
//A0 - led control, for now: 13

//things until miniLock works
//A5 & A4

void setup() {
  //starts as a transmittor
  currentState = 1;
  Serial.begin(9600);

  RF.begin();
// not must default 2  
//  RF.setChannel (18);


// not must
// only to change channel  and/or address
  RF.setTXaddr (1,lightAddr);
  RF.setRXaddr (1,addr);

  
  if (!currentState)
    RF.setRX();
  else
    RF.setTX();
    
  //device need 1.5 ms to reach standby mode (CE=low)
  delay(100);
  
//  setMiniLight();
//  setMiniLock();
}

byte i,j;

void loop() {
   byte bufferin,bufferout;

   if(currentState && command != IDLE_STATE) {
      bufferout = dat;
      RF.send(&bufferout);
      if(dat[i]) i++;
      else {
        delay(50);
      }
   } else {
      if(RF.readReady(&pipe)) {
          RF.read(&bufferin);
          if(datin[j]) j++;
          else {
            j-0;
            for (i=0;dat[i];i++)
            Serial.write (dat[i]);
            Serial.println("");
            delay(50);
          }
      }
   }
  
  if(command != CONFIRM) {
    String commandStr = readFromComputer();
    command = commandStr.charAt(0);
  }
    if(command == LIGHT_ON) {
      lightOn();
    } else if(command == LIGHT_OFF) {
      lightOff();
    } else if(command == LOCK) {
//      Serial.println("locking");
      lock();
    } else if(command == UNLOCK) {
//      Serial.println("unlocking");
      unlock();
    } else if(command == CONFIRM) {
      confirm();
      command = IDLE_STATE;
    } else if(command = IDLE_STATE) {
      
    }
}

String readFromComputer() {
  return Serial.readString();
}

void confirm() {
  Serial.println(CONFIRM);
}

void setMiniLight() {
  pinMode(13, OUTPUT);
}

void lightOn() {
  digitalWrite(13, HIGH);
  delay(100);
  command = CONFIRM;
}

void lightOff() {
  digitalWrite(13, LOW);
  delay(100);
  command = CONFIRM;
}

void setMiniLock() {
  pinMode(A5, OUTPUT);
  pinMode(A4, OUTPUT);
  digitalWrite(A5, LOW);
  digitalWrite(A4, LOW);
}

void lock() {
  digitalWrite(A5, LOW);
  digitalWrite(A4, LOW);
  delay(100);
  command = CONFIRM;
}

void unlock() {
  digitalWrite(A5, LOW);
  digitalWrite(A4, HIGH);
  delay(100);
  command = CONFIRM;
}
