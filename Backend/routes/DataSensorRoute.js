import express from 'express';
import {
    createDataSensor,
    getAllData,
    getTempBySearch,
    getHumidBySearch,
    getLightBySearch,
    getTimeBySearch,
    countHumidHigh,
    sortIncreaseTemp,
    sortDecreaseTemp,
    sortIncreaseHumid,
    sortDecreaseHumid,
    sortIncreaseLight,
    sortDecreaseLight,
    sortIncreaseTime,
    sortDecreaseTime
} from '../controllers/DataSensorController.js';

const router = express.Router();

/**
 * @swagger
 * /datasensor/:
 *   post:
 *     summary: Create data sensor
 *     description: Creates a new data sensor entry.
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               temp:
 *                 type: number
 *               humid:
 *                 type: number
 *               light:
 *                 type: number
 *               time:
 *                 type: string
 *                 format: date-time
 *     responses:
 *       200:
 *         description: Successfully created
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 success:
 *                   type: boolean
 *                 message:
 *                   type: string
 *                 data:
 *                   type: object
 *       500:
 *         description: Failed to create
 */
router.post('/', createDataSensor);

/**
 * @swagger
 * /datasensor/:
 *   get:
 *     summary: Get all data sensors
 *     description: Retrieves all data sensor entries.
 *     responses:
 *       200:
 *         description: Successfully retrieved
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 success:
 *                   type: boolean
 *                 count:
 *                   type: integer
 *                 message:
 *                   type: string
 *                 data:
 *                   type: array
 *                   items:
 *                     type: object
 *       404:
 *         description: Not found
 */
router.get('/', getAllData);

/**
 * @swagger
 * /datasensor/searchTemp:
 *   get:
 *     summary: Search data by temperature
 *     description: Retrieves data sensor entries based on the specified temperature.
 *     parameters:
 *       - name: temp
 *         in: query
 *         required: true
 *         description: Temperature to search for.
 *         schema:
 *           type: string
 *     responses:
 *       200:
 *         description: Successfully retrieved
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 success:
 *                   type: boolean
 *                 count:
 *                   type: integer
 *                 message:
 *                   type: string
 *                 data:
 *                   type: array
 *                   items:
 *                     type: object
 *       404:
 *         description: Not found
 */
router.get('/searchTemp', getTempBySearch);

/**
 * @swagger
 * /datasensor/searchHumid:
 *   get:
 *     summary: Search data by humidity
 *     description: Retrieves data sensor entries based on the specified humidity.
 *     parameters:
 *       - name: humid
 *         in: query
 *         required: true
 *         description: Humidity to search for.
 *         schema:
 *           type: string
 *     responses:
 *       200:
 *         description: Successfully retrieved
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 success:
 *                   type: boolean
 *                 count:
 *                   type: integer
 *                 message:
 *                   type: string
 *                 data:
 *                   type: array
 *                   items:
 *                     type: object
 *       404:
 *         description: Not found
 */
router.get('/searchHumid', getHumidBySearch);

/**
 * @swagger
 * /datasensor/searchLight:
 *   get:
 *     summary: Search data by light
 *     description: Retrieves data sensor entries based on the specified light level.
 *     parameters:
 *       - name: light
 *         in: query
 *         required: true
 *         description: Light level to search for.
 *         schema:
 *           type: string
 *     responses:
 *       200:
 *         description: Successfully retrieved
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 success:
 *                   type: boolean
 *                 count:
 *                   type: integer
 *                 message:
 *                   type: string
 *                 data:
 *                   type: array
 *                   items:
 *                     type: object
 *       404:
 *         description: Not found
 */
router.get('/searchLight', getLightBySearch);

/**
 * @swagger
 * /datasensor/searchTime:
 *   get:
 *     summary: Search data by time
 *     description: Retrieves data sensor entries based on the specified time.
 *     parameters:
 *       - name: time
 *         in: query
 *         required: true
 *         description: Time to search for (can be full date or time).
 *         schema:
 *           type: string
 *     responses:
 *       200:
 *         description: Successfully retrieved
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 success:
 *                   type: boolean
 *                 count:
 *                   type: integer
 *                 message:
 *                   type: string
 *                 data:
 *                   type: array
 *                   items:
 *                     type: object
 *       404:
 *         description: Not found
 */
router.get('/searchTime', getTimeBySearch);

/**
 * @swagger
 * /datasensor/sortTemp/increase:
 *   get:
 *     summary: Sort data by temperature in increasing order
 *     description: Retrieves data sensor entries sorted by temperature in increasing order.
 *     responses:
 *       200:
 *         description: Successfully retrieved
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 success:
 *                   type: boolean
 *                 count:
 *                   type: integer
 *                 message:
 *                   type: string
 *                 data:
 *                   type: array
 *                   items:
 *                     type: object
 *       404:
 *         description: Not found
 */
router.get('/sortTemp/increase', sortIncreaseTemp);

/**
 * @swagger
 * /datasensor/sortTemp/decrease:
 *   get:
 *     summary: Sort data by temperature in decreasing order
 *     description: Retrieves data sensor entries sorted by temperature in decreasing order.
 *     responses:
 *       200:
 *         description: Successfully retrieved
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 success:
 *                   type: boolean
 *                 count:
 *                   type: integer
 *                 message:
 *                   type: string
 *                 data:
 *                   type: array
 *                   items:
 *                     type: object
 *       404:
 *         description: Not found
 */
router.get('/sortTemp/decrease', sortDecreaseTemp);

/**
 * @swagger
 * /datasensor/sortHumid/increase:
 *   get:
 *     summary: Sort data by humidity in increasing order
 *     description: Retrieves data sensor entries sorted by humidity in increasing order.
 *     responses:
 *       200:
 *         description: Successfully retrieved
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 success:
 *                   type: boolean
 *                 count:
 *                   type: integer
 *                 message:
 *                   type: string
 *                 data:
 *                   type: array
 *                   items:
 *                     type: object
 *       404:
 *         description: Not found
 */
router.get('/sortHumid/increase', sortIncreaseHumid);

/**
 * @swagger
 * /datasensor/sortHumid/decrease:
 *   get:
 *     summary: Sort data by humidity in decreasing order
 *     description: Retrieves data sensor entries sorted by humidity in decreasing order.
 *     responses:
 *       200:
 *         description: Successfully retrieved
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 success:
 *                   type: boolean
 *                 count:
 *                   type: integer
 *                 message:
 *                   type: string
 *                 data:
 *                   type: array
 *                   items:
 *                     type: object
 *       404:
 *         description: Not found
 */
router.get('/sortHumid/decrease', sortDecreaseHumid);

/**
 * @swagger
 * /datasensor/sortLight/increase:
 *   get:
 *     summary: Sort data by light in increasing order
 *     description: Retrieves data sensor entries sorted by light in increasing order.
 *     responses:
 *       200:
 *         description: Successfully retrieved
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 success:
 *                   type: boolean
 *                 count:
 *                   type: integer
 *                 message:
 *                   type: string
 *                 data:
 *                   type: array
 *                   items:
 *                     type: object
 *       404:
 *         description: Not found
 */
router.get('/sortLight/increase', sortIncreaseLight);

/**
 * @swagger
 * /datasensor/sortLight/decrease:
 *   get:
 *     summary: Sort data by light in decreasing order
 *     description: Retrieves data sensor entries sorted by light in decreasing order.
 *     responses:
 *       200:
 *         description: Successfully retrieved
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 success:
 *                   type: boolean
 *                 count:
 *                   type: integer
 *                 message:
 *                   type: string
 *                 data:
 *                   type: array
 *                   items:
 *                     type: object
 *       404:
 *         description: Not found
 */
router.get('/sortLight/decrease', sortDecreaseLight);

/**
 * @swagger
 * /datasensor/sortTime/increase:
 *   get:
 *     summary: Sort data by time in increasing order
 *     description: Retrieves data sensor entries sorted by time in increasing order.
 *     responses:
 *       200:
 *         description: Successfully retrieved
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 success:
 *                   type: boolean
 *                 count:
 *                   type: integer
 *                 message:
 *                   type: string
 *                 data:
 *                   type: array
 *                   items:
 *                     type: object
 *       404:
 *         description: Not found
 */
router.get('/sortTime/increase', sortIncreaseTime);

/**
 * @swagger
 * /datasensor/sortTime/decrease:
 *   get:
 *     summary: Sort data by time in decreasing order
 *     description: Retrieves data sensor entries sorted by time in decreasing order.
 *     responses:
 *       200:
 *         description: Successfully retrieved
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 success:
 *                   type: boolean
 *                 count:
 *                   type: integer
 *                 message:
 *                   type: string
 *                 data:
 *                   type: array
 *                   items:
 *                     type: object
 *       404:
 *         description: Not found
 */
router.get('/sortTime/decrease', sortDecreaseTime);

/**
 * @swagger
 * /datasensor/countHumidHigh:
 *   get:
 *     summary: Count high humidity entries
 *     description: Counts the number of data sensor entries with humidity greater than 80% for the current day.
 *     responses:
 *       200:
 *         description: Successfully counted
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 success:
 *                   type: boolean
 *                 count:
 *                   type: integer
 *                 message:
 *                   type: string
 *       404:
 *         description: Not found
 */
router.get('/countHumidHigh', countHumidHigh);

export default router;