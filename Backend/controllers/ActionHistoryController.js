import ActionHistory from '../models/ActionHistory.js';
import mqtt from 'mqtt';

const options = {
    username: 'Nguyen_Thi_Quyen', 
    password: 'b21dccn639',  
};

const client = mqtt.connect('mqtt:192.168.189.2:1993', options);

client.on('connect', () => {
    console.log('Connected to MQTT broker');
});

client.on('error', (error) => {
    console.error('MQTT Client Error:', error);
});

const convertState = (actionHistory) => {
    let res; 

    if (actionHistory.device === 'Fan') {
        if (actionHistory.action === 'On') {
            res = 'LED1_ON';
        } else {
            res = 'LED1_OFF';
        }
    } else if (actionHistory.device === 'Light') {
        if (actionHistory.action === 'On') {
            res = 'LED2_ON';
        } else {
            res = 'LED2_OFF';
        }
    } else if (actionHistory.device === 'Conditioner') {
        if (actionHistory.action === 'On') {
            res = 'LED3_ON';
        } else {
            res = 'LED3_OFF';
        }
    } else if(actionHistory.device === 'Curtain') {
        if (actionHistory.action === 'Open') {
            res = 'LED4_ON';
        } else {
            res = 'LED4_OFF';
        }
    } else if(actionHistory.device === 'Humidifier') {
        if (actionHistory.action === 'On') {
            res = 'LED5_ON';
        } else {
            res = 'LED5_OFF';
        }
    } else if(actionHistory.device === 'Heater') {
        if (actionHistory.action === 'On') {
            res = 'LED6_ON';
        } else {
            res = 'LED6_OFF';
        }
    } else {
        res = 'UNKNOWN_DEVICE'; 
    }

    return res; 
}

// create history
export const createActionHistory = async (req, res) => {
    const newHistory = new ActionHistory(req.body);

    try {
        const savedHistory = await newHistory.save();

        // send message MQTT
        const message = convertState(savedHistory); 

        client.publish('actionhistory', message, { qos: 1 }, (error) => {
            if (error) {
                console.error('Failed to publish message to MQTT:', error);
            } else {
                console.log('Message sent to MQTT:', message);
            }
        });

        res.status(200).json({ success: true, message: 'Successfully created', data: savedHistory });
    } catch (error) {
        console.error('Error creating history:', error);
        res.status(500).json({ success: false, message: 'Failed to create. Try again!' });
    }
};

export const getTimeBySearch = async (req, res) => {
    const searchText = req.query.time; 

    try {
        const actionHistories = await ActionHistory.find({
            time: {
                $regex: new RegExp(searchText, 'i'),
            }
        });

        res.status(200).json({ success: true, count: actionHistories.length, message: 'Successfully', data: actionHistories });
    } catch (error) {
        res.status(404).json({success: false, message: 'Not found!'})
    }
};

export const getActionHistory = async (req, res) => {
    const page = parseInt(req.query.page)
 
    try {
       const actionHistorys = await ActionHistory.find({}).skip(page * 50).limit(50)
 
       res.status(200).json({ success: true, count: actionHistorys.length, message: 'Successfully', data: actionHistorys })
    } catch (error) {
       res.status(404).json({ success: false, message: 'Not Found' })
    }
 }
