import DataSensor from '../models/DataSensor.js'
import { format } from 'date-fns';

// create new data
export const createDataSensor = async (req, res) => {
    const { temp, humid, light, time } = req.body;

    const date = new Date(time);
    const formattedTime = format(date, 'yyyy-MM-dd HH:mm:ss');

    const newDataSensor = new DataSensor({
        temp,
        humid,
        light,
        time: formattedTime, 
    });

    try {
        const savedData = await newDataSensor.save();
        res.status(200).json({ success: true, message: 'Successfully created', data: savedData });
    } catch (error) {
        console.error('Error saving data:', error);
        res.status(500).json({ success: false, message: 'Failed to create. Try again!' });
    }
};

export const getTempBySearch = async (req, res) => {
    const searchText = req.query.temp; 

    try {
        const dataSensors = await DataSensor.find({ temp: { $regex: new RegExp(searchText, 'i') } })

        res.status(200).json({ success: true, count: dataSensors.length, message: 'Successfully', data: dataSensors });
    } catch (error) {
        res.status(404).json({ success: false, message: 'Not Found' });
    }
};

export const getHumidBySearch = async (req, res) => {
    const searchText = req.query.humid; 

    try {
        const dataSensors = await DataSensor.find({ humid: { $regex: new RegExp(searchText, 'i') } })

        res.status(200).json({ success: true, count: dataSensors.length, message: 'Successfully', data: dataSensors });
    } catch (error) {
        res.status(404).json({ success: false, message: 'Not Found' });
    }
};

export const getLightBySearch = async (req, res) => {
    const searchText = req.query.light; 

    try {
        const dataSensors = await DataSensor.find({ light: { $regex: new RegExp(searchText, 'i') } })

        res.status(200).json({ success: true, count: dataSensors.length, message: 'Successfully', data: dataSensors });
    } catch (error) {
        res.status(404).json({ success: false, message: 'Not Found' });
    }
};

export const getTimeBySearch = async (req, res) => {
    const searchText = req.query.time; 

    try {
        const dataSensors = await DataSensor.find({ time: { $regex: new RegExp(searchText, 'i') } })

        res.status(200).json({ success: true, count: dataSensors.length, message: 'Successfully', data: dataSensors });
    } catch (error) {
        res.status(404).json({success: false, message: 'Not found!'})
    }
};

export const sortIncreaseTemp = async (req, res) => {
    const page = parseInt(req.query.page)

    try {
        const dataSensor = await DataSensor.find().sort({ temp : 1}).skip(page * 50).limit(50)
        res.status(200).json({success: true, count: dataSensor.length, message: 'Successfully', data: dataSensor})
    } catch(error) {
        res.status(404).json({success: false, message: 'Not found!'})
    }
}

export const sortDecreaseTemp = async (req, res) => {
    const page = parseInt(req.query.page)

    try {
        const dataSensor = await DataSensor.find().sort({ temp : -1}).skip(page * 50).limit(50)
        res.status(200).json({success: true, count: dataSensor.length, message: 'Successfully', data: dataSensor})
    } catch(error) {
        res.status(404).json({success: false, message: 'Not found!'})
    }
}

export const sortIncreaseHumid = async (req, res) => {
    const page = parseInt(req.query.page)

    try {
        const dataSensor = await DataSensor.find().sort({ humid : 1}).skip(page * 50).limit(50)
        res.status(200).json({success: true, count: dataSensor.length, message: 'Successfully', data: dataSensor})
    } catch(error) {
        res.status(404).json({success: false, message: 'Not found!'})
    }
}

export const sortDecreaseHumid = async (req, res) => {
    const page = parseInt(req.query.page) 

    try {
        const dataSensor = await DataSensor.find().sort({ humid : -1}).skip(page * 50).limit(50)
        res.status(200).json({success: true, count: dataSensor.length, message: 'Successfully', data: dataSensor})
    } catch(error) {
        res.status(404).json({success: false, message: 'Not found!'})
    }
}

export const sortIncreaseLight = async (req, res) => {
    const page = parseInt(req.query.page)

    try {
        const dataSensor = await DataSensor.find().sort({ light : 1}).skip(page * 50).limit(50)
        res.status(200).json({success: true, count: dataSensor.length, message: 'Successfully', data: dataSensor})
    } catch(error) {
        res.status(404).json({success: false, message: 'Not found!'})
    }
}

export const sortDecreaseLight = async (req, res) => {
    const page = parseInt(req.query.page)

    try {
        const dataSensor = await DataSensor.find().sort({ light : -1}).skip(page * 50).limit(50)
        res.status(200).json({success: true, count: dataSensor.length, message: 'Successfully', data: dataSensor})
    } catch(error) {
        res.status(404).json({success: false, message: 'Not found!'})
    }
}

export const sortIncreaseTime = async (req, res) => {
    const page = parseInt(req.query.page)

    try {
        const dataSensor = await DataSensor.find().sort({ time : 1}).skip(page * 50).limit(50)
        res.status(200).json({success: true, count: dataSensor.length, message: 'Successfully', data: dataSensor})
    } catch(error) {
        res.status(404).json({success: false, message: 'Not found!'})
    }
}

export const sortDecreaseTime = async (req, res) => {
    const page = parseInt(req.query.page)

    try {
        const dataSensor = await DataSensor.find().sort({ time : -1}).skip(page * 50).limit(50)
        res.status(200).json({success: true, count: dataSensor.length, message: 'Successfully', data: dataSensor})
    } catch(error) {
        res.status(404).json({success: false, message: 'Not found!'})
    }
}

//Get datasensor page
export const getDataSensor = async (req, res) => {
    const page = parseInt(req.query.page)
 
    try {
       const datasensors = await DataSensor.find({}).skip(page * 50).limit(50)
 
       res.status(200).json({ success: true, count: datasensors.length, message: 'Successfully', data: datasensors })
    } catch (error) {
       res.status(404).json({ success: false, message: 'Not Found' })
    }
 }

 export const getTenDataSensor = async (req, res) => {
 
    try {
       const datasensors = await DataSensor.find({}).sort({ time : -1}).limit(10)
 
       res.status(200).json({ success: true, count: datasensors.length, message: 'Successfully', data: datasensors })
    } catch (error) {
       res.status(404).json({ success: false, message: 'Not Found' })
    }
 }
