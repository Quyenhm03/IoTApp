import ActionHistory from '../models/ActionHistory.js';
import mqtt from 'mqtt';

const options = {
    username: 'Nguyen_Thi_Quyen', 
    password: 'b21dccn639',  
};

const client = mqtt.connect('mqtt:192.168.1.9:1993', options);

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

// get all history
export const getAllHistory = async (req, res) => {
    try {
        const actionHistory = await ActionHistory.find({})
        res.status(200).json({success: true, count: actionHistory.length, message: 'Successfully', data: actionHistory})
    } catch(error) {
        res.status(404).json({success: false, message: 'Not found!'})
    }
}

export const getTimeBySearch = async (req, res) => {
    const searchText = req.query.time; 

    let startOfHour, endOfHour;

    const fullDateRegex = /^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/; 
    const dateOnlyRegex = /^\d{4}-\d{2}-\d{2}$/; 
    const timeOnlyRegex = /^\d{2}:\d{2}:\d{2}$/;

    if (fullDateRegex.test(searchText)) {
        const searchDate = new Date(searchText);
        startOfHour = new Date(searchDate);
        startOfHour.setMilliseconds(0); 

        endOfHour = new Date(searchDate);
        endOfHour.setMilliseconds(999); 

        try {
            const actionHistories = await ActionHistory.find({
                time: {
                    $gte: startOfHour.toISOString(),
                    $lte: endOfHour.toISOString()
                }
            });
    
            res.status(200).json({ success: true, count: actionHistories.length, message: 'Successfully', data: actionHistories });
        } catch (error) {
            res.status(404).json({success: false, message: 'Not found!'})
        }

    } else if (dateOnlyRegex.test(searchText)) {
        const searchDate = new Date(searchText);
        startOfHour = new Date(searchDate);
        startOfHour.setHours(0, 0, 0, 0); 

        endOfHour = new Date(searchDate);
        endOfHour.setHours(23, 59, 59, 999);

        try {
            const actionHistories = await ActionHistory.find({
                time: {
                    $gte: startOfHour.toISOString(),
                    $lte: endOfHour.toISOString()
                }
            });
    
            res.status(200).json({ success: true, count: actionHistories.length, message: 'Successfully', data: actionHistories });
        } catch (error) {
            res.status(404).json({success: false, message: 'Not found!'})
        }

    } else if (timeOnlyRegex.test(searchText)) {
        const [hours, minutes, seconds] = searchText.split(':').map(Number);

        try {
            const actionHistories = await ActionHistory.find({
                $expr: {
                    $and: [
                        { $eq: [{ $hour: "$time" }, hours-7] },
                        { $eq: [{ $minute: "$time" }, minutes] },
                        { $eq: [{ $second: "$time" }, seconds] }
                    ]
                }
            });
            res.status(200).json({ success: true, count: actionHistories.length, message: 'Successfully', data: actionHistories });
        } catch (error) {
            res.status(404).json({ success: false, message: 'Not Found' });
        }
    }
};

export const warningHigh = async (req, res) => {
    const message = req.body.message; 

    client.publish('actionhistory', message, { qos: 1 }, (error) => {
        if (error) {
            console.error('Failed to publish message to MQTT:', error);
            return res.status(500).json({ success: false, message: 'Failed to send message' });
        } else {
            console.log('Message sent to MQTT:', message);
            return res.status(200).json({ success: true, message: 'Message sent successfully', data: message });
        }
    });
};