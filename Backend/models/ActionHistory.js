import mongoose from 'mongoose'

const ActionHistorySchema = new mongoose.Schema (
    {
        device: {
            type: String,
            required: true,
        },
        action: {
            type: String,
            required: true,
        }, 
        time: {
            type: Date,
            required: true,
        }
    }
);

export default mongoose.model("ActionHistory", ActionHistorySchema);