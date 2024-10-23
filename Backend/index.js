import express from 'express'
import dotenv from 'dotenv'
import mongoose from 'mongoose'
import './controllers/MqttController.js'
import DataSensorRoute from './routes/DataSensorRoute.js'
import ActionHistoryRoute from './routes/ActionHistoryRoute.js'
import swaggerUi from 'swagger-ui-express'
import swaggerJSDocs from './swaggerConfig.js'

dotenv.config()

const app = express()
const port = process.env.PORT
const connect = async() => {
    try{
        await mongoose.connect(process.env.MONGO_URI)
        console.log('MongoDB connected')
    } catch(error) {
        console.log('MongoDB connected failed')
    }
}

app.use(express.json())
app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(swaggerJSDocs));
app.use('/datasensor', DataSensorRoute)
app.use('/actionhistory', ActionHistoryRoute)

app.listen(port, () => {
    connect()
    console.log('server listening on port', port)
})