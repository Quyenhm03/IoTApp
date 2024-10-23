import mongoose from 'mongoose'

const DataSensorSchema = new mongoose.Schema(
    {
        temp: {
            type: Number,
            required: true,
        }, 
        humid: {
            type: Number,
            required: true,
        },
        light: {
            type: Number,
            required: true,
        },
        time: {
            type: Date,
            required: true,
        }
    }
);

export default mongoose.model("DataSensor", DataSensorSchema);