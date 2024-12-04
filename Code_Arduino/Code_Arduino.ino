#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <DHT.h>
#include <WiFiUdp.h>
#include <NTPClient.h>
#include <time.h>

#define DHTPIN D2        // Chân DATA của DHT11
#define DHTTYPE DHT11    // Loại cảm biến DHT
DHT dht(DHTPIN, DHTTYPE);

#define LED1 D5         // Chân điều khiển LED1
#define LED2 D6         // Chân điều khiển LED2
#define LED3 D7         // Chân điều khiển LED3
#define LED4 D8
#define LED5 D3
#define LED6 D0
#define LDR_PIN D1      // Chân đọc giá trị từ cảm biến ánh sáng

const char* ssid = "";             
const char* password = "";  
const char* mqttServer = "192.168.189.2"; 
const int mqttPort = 1993;              
const char* mqttUser = "Nguyen_Thi_Quyen"; 
const char* mqttPassword = "b21dccn639"; 

bool led4State = false;  // Biến cờ để theo dõi trạng thái LED4
unsigned long ledPreviousMillis = 0; // Thời gian trước đó cho LED
const long ledOnTime = 500; // Thời gian bật LED4 (ms)
const long ledOffTime = 100; // Thời gian tắt LED4 (ms)

unsigned long lastDataMillis = 0; // Thời gian gửi dữ liệu trước đó
const long dataInterval = 5000; // Khoảng thời gian gửi dữ liệu (ms)

WiFiClient espClient;
PubSubClient client(espClient);
WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP, "pool.ntp.org", 3600 * 7, 60000); // UTC+7

void setup() {
  Serial.begin(9600);
  dht.begin();
  
  pinMode(LED1, OUTPUT);
  pinMode(LED2, OUTPUT);
  pinMode(LED3, OUTPUT);
  pinMode(LED4, OUTPUT);
  pinMode(LED5, OUTPUT);
  pinMode(LED6, OUTPUT);
  pinMode(LDR_PIN, INPUT);

  // Kết nối WiFi
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.println("Connecting to WiFi...");
  }
  Serial.println("Connected to WiFi");

  // Thiết lập máy chủ MQTT
  client.setServer(mqttServer, mqttPort);
  client.setCallback(callback);
  
  // Bắt đầu NTPClient
  timeClient.begin();
}

void loop() {
  if (!client.connected()) {
    reconnect();
  }
  client.loop();

  // Cập nhật thời gian từ NTP
  timeClient.update();

  // Gửi dữ liệu lên MQTT mỗi 5 giây
  unsigned long currentMillis = millis();
  if (currentMillis - lastDataMillis >= dataInterval) {
    lastDataMillis = currentMillis;

    // Đọc dữ liệu từ DHT11
    float h = dht.readHumidity();
    float t = dht.readTemperature();

    // Đọc giá trị từ cảm biến ánh sáng
    int ldrValue = digitalRead(LDR_PIN);

    // Gửi dữ liệu lên MQTT
    float l = (ldrValue == HIGH) ? 350 + (random(0, 2000) / 1000.0) : 150 + (random(0, 2000) / 1000.0);
    
    // Lấy thời gian và định dạng theo yêu cầu
    String formattedTime = formatTime(timeClient.getEpochTime());
    String dataSensor = "temp: " + String(temp, 2) + ", humid: " + String(humid, 2) + ", light: " + String(l, 2) + ", time: " + formattedTime ;
    client.publish("datasensor", dataSensor.c_str());

    // In ra giá trị độ ẩm, nhiệt độ và trạng thái ánh sáng
    Serial.println(dataSensor);
  }
}

String formatTime(unsigned long epochTime) {
  time_t rawTime = (time_t)epochTime;
  struct tm *timeInfo;
  timeInfo = localtime(&rawTime);

  // Chuyển đổi ngày, tháng, năm
  int day = timeInfo->tm_mday;
  int month = timeInfo->tm_mon + 1; // Tháng từ 0-11
  int year = timeInfo->tm_year + 1900; // Năm từ 1900

  // Chuyển đổi giờ, phút, giây
  int hours = timeInfo->tm_hour;
  int minutes = timeInfo->tm_min;
  int seconds = timeInfo->tm_sec;

  // Định dạng chuỗi theo "YYYY-MM-DDTHH:MM:SS"
  String formattedTime = String(year) + "-" +
                         (month < 10 ? "0" : "") + String(month) + "-" +
                         (day < 10 ? "0" : "") + String(day) + "T" +
                         (hours < 10 ? "0" : "") + String(hours) + ":" +
                         (minutes < 10 ? "0" : "") + String(minutes) + ":" +
                         (seconds < 10 ? "0" : "") + String(seconds);
  return formattedTime;
}

void reconnect() {
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    if (client.connect("ESP8266Client", mqttUser, mqttPassword)) {
      Serial.println("connected");
      client.subscribe("actionhistory");
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" trying again in 5 seconds");
      delay(5000); // Chờ 5 giây trước khi thử lại
    }
  }
}

void callback(char* topic, byte* payload, unsigned int length) {
  payload[length] = '\0';
  String message = String((char*)payload);

  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("]: ");
  Serial.println(message);
  
  // Điều khiển LED dựa trên nội dung tin nhắn
  if (message == "LED1_ON") {
    digitalWrite(LED1, HIGH);
  } else if (message == "LED1_OFF") {
    digitalWrite(LED1, LOW);
  } else if (message == "LED2_ON") {
    digitalWrite(LED2, HIGH);
  } else if (message == "LED2_OFF") {
    digitalWrite(LED2, LOW);
  } else if (message == "LED3_ON") {
    digitalWrite(LED3, HIGH);
  } else if (message == "LED3_OFF") {
    digitalWrite(LED3, LOW);
  } else if (message == "LED4_ON") {  
    digitalWrite(LED4, HIGH); 
  } else if (message == "LED4_OFF") {
    digitalWrite(LED4, LOW); 
  }  else if (message == "LED5_ON") {  
    digitalWrite(LED5, HIGH); 
  } else if (message == "LED5_OFF") {
    digitalWrite(LED5, LOW); 
  }  else if (message == "LED6_ON") {  
    digitalWrite(LED6, HIGH); 
  } else if (message == "LED6_OFF") {
    digitalWrite(LED6, LOW); 
  } 

}