import DataSensor from '../models/DataSensor.js'

// create new data
export const createDataSensor = async (req, res) => {
    const newDataSensor = new DataSensor(req.body);
    try {
        const savedData = await newDataSensor.save();
        res.status(200).json({ success: true, message: 'Successfully', data: savedData });
    } catch (error) {
        console.error('Error saving data:', error);
        res.status(500).json({ success: false, message: 'Failed to create. Try again!' });
    }
};

// get all data
export const getAllData = async (req, res) => {
    try {
        const dataSensor = await DataSensor.find({})
        res.status(200).json({success: true, count: dataSensor.length, message: 'Successfully', data: dataSensor})
    } catch(error) {
        res.status(404).json({success: false, message: 'Not found!'})
    }
}

export const getTempBySearch = async (req, res) => {
    const searchText = req.query.temp; 
    const searchTemp = parseFloat(searchText);

    let minTemp, maxTemp;

    const decimalPlaces = (searchText.split('.')[1] || []).length;

    if (decimalPlaces === 0 || searchTemp % 1 === 0) { 
        minTemp = searchTemp; 
        maxTemp = searchTemp + 0.99; 
    } else if (decimalPlaces === 1 || searchTemp*10 % 1 === 0) {
        minTemp = searchTemp;
        maxTemp = searchTemp + 0.09;
    } else { 
        minTemp = parseFloat(searchText.split('.')[0] + '.' + searchText.split('.')[1].slice(0, 2));
        maxTemp = minTemp;
    }

    try {
        const dataSensors = await DataSensor.find({
            temp: { $gte: minTemp, $lte: maxTemp } 
        });

        res.status(200).json({ success: true, count: dataSensors.length, message: 'Successfully', data: dataSensors });
    } catch (error) {
        res.status(404).json({ success: false, message: 'Not Found' });
    }
};

export const getHumidBySearch = async (req, res) => {
    const searchText = req.query.humid; 
    const searchHumid = parseFloat(searchText);

    let minHumid, maxHumid;

    const decimalPlaces = (searchText.split('.')[1] || []).length;

    if (decimalPlaces === 0 || searchHumid % 1 === 0) { 
        minHumid = searchHumid; 
        maxHumid = searchHumid + 0.99; 
    } else if (decimalPlaces === 1 || searchHumid*10 % 1 === 0) {
        minHumid = searchHumid;
        maxHumid = searchHumid + 0.09;
    } else { 
        minHumid = parseFloat(searchText.split('.')[0] + '.' + searchText.split('.')[1].slice(0, 2));
        maxHumid = minHumid;
    }

    try {
        const dataSensors = await DataSensor.find({
            humid: { $gte: minHumid, $lte: maxHumid } 
        });

        res.status(200).json({ success: true, count: dataSensors.length, message: 'Successfully', data: dataSensors });
    } catch (error) {
        res.status(404).json({ success: false, message: 'Not Found' });
    }
};

export const getLightBySearch = async (req, res) => {
    const searchText = req.query.light; 
    const searchLight = parseFloat(searchText);

    let minLight, maxLight;

    const decimalPlaces = (searchText.split('.')[1] || []).length;

    if (decimalPlaces === 0 || searchLight % 1 === 0) { 
        minLight = searchLight; 
        maxLight = searchLight + 0.99; 
    } else if (decimalPlaces === 1 || searchLight*10 % 1 === 0) {
        minLight = searchLight;
        maxLight = searchLight + 0.09;
    } else { 
        minLight = parseFloat(searchText.split('.')[0] + '.' + searchText.split('.')[1].slice(0, 2));
        maxLight = minLight;
    }

    try {
        const dataSensors = await DataSensor.find({
            light: { $gte: minLight, $lte: maxLight } 
        });

        res.status(200).json({ success: true, count: dataSensors.length, message: 'Successfully', data: dataSensors });
    } catch (error) {
        res.status(404).json({ success: false, message: 'Not Found' });
    }
};

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
            const dataSensors = await DataSensor.find({
                time: {
                    $gte: startOfHour.toISOString(),
                    $lte: endOfHour.toISOString()
                }
            });
    
            res.status(200).json({ success: true, count: dataSensors.length, message: 'Successfully', data: dataSensors });
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
            const dataSensors = await DataSensor.find({
                time: {
                    $gte: startOfHour.toISOString(),
                    $lte: endOfHour.toISOString()
                }
            });
    
            res.status(200).json({ success: true, count: dataSensors.length, message: 'Successfully', data: dataSensors });
        } catch (error) {
            res.status(404).json({success: false, message: 'Not found!'})
        }

    } else if (timeOnlyRegex.test(searchText)) {
        const [hours, minutes, seconds] = searchText.split(':').map(Number);

        try {
            const dataSensors = await DataSensor.find({
                $expr: {
                    $and: [
                        { $eq: [{ $hour: "$time" }, hours-7] },
                        { $eq: [{ $minute: "$time" }, minutes] },
                        { $eq: [{ $second: "$time" }, seconds] }
                    ]
                }
            });
            res.status(200).json({ success: true, count: dataSensors.length, message: 'Successfully', data: dataSensors });
        } catch (error) {
            res.status(404).json({ success: false, message: 'Not Found' });
        }
    }
};

export const sortIncreaseTemp = async (req, res) => {
    try {
        const dataSensor = await DataSensor.find().sort({ temp : 1})
        res.status(200).json({success: true, count: dataSensor.length, message: 'Successfully', data: dataSensor})
    } catch(error) {
        res.status(404).json({success: false, message: 'Not found!'})
    }
}

export const sortDecreaseTemp = async (req, res) => {
    try {
        const dataSensor = await DataSensor.find().sort({ temp : -1})
        res.status(200).json({success: true, count: dataSensor.length, message: 'Successfully', data: dataSensor})
    } catch(error) {
        res.status(404).json({success: false, message: 'Not found!'})
    }
}

export const sortIncreaseHumid = async (req, res) => {
    try {
        const dataSensor = await DataSensor.find().sort({ humid : 1})
        res.status(200).json({success: true, count: dataSensor.length, message: 'Successfully', data: dataSensor})
    } catch(error) {
        res.status(404).json({success: false, message: 'Not found!'})
    }
}

export const sortDecreaseHumid = async (req, res) => {
    try {
        const dataSensor = await DataSensor.find().sort({ humid : -1})
        res.status(200).json({success: true, count: dataSensor.length, message: 'Successfully', data: dataSensor})
    } catch(error) {
        res.status(404).json({success: false, message: 'Not found!'})
    }
}

export const sortIncreaseLight = async (req, res) => {
    try {
        const dataSensor = await DataSensor.find().sort({ light : 1})
        res.status(200).json({success: true, count: dataSensor.length, message: 'Successfully', data: dataSensor})
    } catch(error) {
        res.status(404).json({success: false, message: 'Not found!'})
    }
}

export const sortDecreaseLight = async (req, res) => {
    try {
        const dataSensor = await DataSensor.find().sort({ light : -1})
        res.status(200).json({success: true, count: dataSensor.length, message: 'Successfully', data: dataSensor})
    } catch(error) {
        res.status(404).json({success: false, message: 'Not found!'})
    }
}

export const sortIncreaseTime = async (req, res) => {
    try {
        const dataSensor = await DataSensor.find().sort({ time : 1})
        res.status(200).json({success: true, count: dataSensor.length, message: 'Successfully', data: dataSensor})
    } catch(error) {
        res.status(404).json({success: false, message: 'Not found!'})
    }
}

export const sortDecreaseTime = async (req, res) => {
    try {
        const dataSensor = await DataSensor.find().sort({ time : -1})
        res.status(200).json({success: true, count: dataSensor.length, message: 'Successfully', data: dataSensor})
    } catch(error) {
        res.status(404).json({success: false, message: 'Not found!'})
    }
}

export const countHumidHigh = async (req, res) => {
    try {
        const startOfDay = new Date();
        startOfDay.setHours(0, 0, 0, 0); 

        const endOfDay = new Date();
        endOfDay.setHours(23, 59, 59, 999); 

        const dataSensor = await DataSensor.find({
            humid: { $gt: 80 },
            createdAt: { $gte: startOfDay, $lte: endOfDay }
        });

        res.status(200).json({success: true, count: dataSensor.length, message: 'Successfully'});
    } catch (error) {
        res.status(404).json({ success: false, message: 'Not found!' });
    }
};