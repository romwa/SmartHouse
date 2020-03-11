//code for the main arduino


char LIGHT_ON='n';
char LIGHT_OFF='f';
char LOCK='l';
char UNLOCK='u';
char CONFIRM='c';
char IDLE_STATE = 'd';
char command = IDLE_STATE;

//things until miniLight works
//A0 - led control, for now: 13

//things until miniLock works
//A5 & A4

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  setMiniLight();
  setMiniLock();
}

void loop() {
  // put your main code here, to run repeatedly:
//  std::string commandStr = readFromComputer();
//  int command = std::stoi(commandStr);
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
