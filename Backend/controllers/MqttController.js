import mqtt from 'mqtt';
import { createDataSensor } from '../controllers/DataSensorController.js';

const options = {
    username: 'Nguyen_Thi_Quyen', 
    password: 'b21dccn639',  
};

const client = mqtt.connect('mqtt:192.168.1.9:1993', options)

client.on('connect', () => {
    console.log('Connected to MQTT broker');

    client.subscribe('datasensor', (err) => {
        if (!err) {
            console.log('Subscribed to topic: datasensor');
        }
    });

    client.subscribe('actionhistory', (err) => {
        if (!err) {
            console.log('Subscribed to topic: actionhistory');
        }
    });
});

client.on('message', async (topic, message) => {
    const msgString = message.toString();

    if(topic === 'datasensor') {
        const data = {};
        msgString.split(', ').forEach(item => {
            const [key, value] = item.split(': ');
            
            if (key === 'time') {
                data[key] = new Date(value);
            } else {
                data[key] = parseFloat(value); 
            }
        });

        const res = {
            status: (statusCode) => {
                return {
                    json: (responseBody) => {
                        console.log('Response:', statusCode, responseBody); 
                    }
                };
            }
        };

        await createDataSensor({ body: data }, res);
    }
});

client.on('error', (err) => {
    console.error('MQTT error:', err);
});

export default client