import mongoose from 'mongoose'

const DataSensorSchema = new mongoose.Schema(
    {
        temp: {
            type: String,
            required: true,
        }, 
        humid: {
            type: String,
            required: true,
        },
        light: {
            type: String,
            required: true,
        },
        time: {
            type: String,
            required: true,
        }
    }
);

export default mongoose.model("DataSensor", DataSensorSchema);