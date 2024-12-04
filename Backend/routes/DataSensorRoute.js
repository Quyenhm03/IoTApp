import express from 'express';
import {
    createDataSensor,
    getDataSensor,
    getTempBySearch,
    getHumidBySearch,
    getLightBySearch,
    getTimeBySearch,
    sortIncreaseTemp,
    sortDecreaseTemp,
    sortIncreaseHumid,
    sortDecreaseHumid,
    sortIncreaseLight,
    sortDecreaseLight,
    sortIncreaseTime,
    sortDecreaseTime,
    getTenDataSensor
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
 *                 type: string
 *               humid:
 *                 type: string
 *               light:
 *                 type: string
 *               time:
 *                 type: string           
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
 * /datasensor:
 *   get:
 *     summary: Get data sensor
 *     description: Returns a list of data sensor, paginated with 50 records per page.
 *     parameters:
 *       - name: page
 *         in: query
 *         required: true
 *         description: Page number.
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
router.get('/', getDataSensor);

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
 *         description: Time to search for .
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
 *     description: Retrieves data sensor entries sorted by temperature in increasing order, paginated with 50 records per page.
 *     parameters:
 *       - name: page
 *         in: query
 *         required: true
 *         description: Page number.
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
router.get('/sortTemp/increase', sortIncreaseTemp);

/**
 * @swagger
 * /datasensor/sortTemp/decrease:
 *   get:
 *     summary: Sort data by temperature in decreasing order
 *     description: Retrieves data sensor entries sorted by temperature in decreasing order, paginated with 50 records per page.
 *     parameters:
 *       - name: page
 *         in: query
 *         required: true
 *         description: Page number.
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
router.get('/sortTemp/decrease', sortDecreaseTemp);

/**
 * @swagger
 * /datasensor/sortHumid/increase:
 *   get:
 *     summary: Sort data by humidity in increasing order
 *     description: Retrieves data sensor entries sorted by humidity in increasing order.
 *     parameters:
 *       - name: page
 *         in: query
 *         required: true
 *         description: Page number.
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
router.get('/sortHumid/increase', sortIncreaseHumid);

/**
 * @swagger
 * /datasensor/sortHumid/decrease:
 *   get:
 *     summary: Sort data by humidity in decreasing order
 *     description: Retrieves data sensor entries sorted by humidity in decreasing order, paginated with 50 records per page.
 *     parameters:
 *       - name: page
 *         in: query
 *         required: true
 *         description: Page number.
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
router.get('/sortHumid/decrease', sortDecreaseHumid);

/**
 * @swagger
 * /datasensor/sortLight/increase:
 *   get:
 *     summary: Sort data by light in increasing order
 *     description: Retrieves data sensor entries sorted by light in increasing order, paginated with 50 records per page.
 *     parameters:
 *       - name: page
 *         in: query
 *         required: true
 *         description: Page number.
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
router.get('/sortLight/increase', sortIncreaseLight);

/**
 * @swagger
 * /datasensor/sortLight/decrease:
 *   get:
 *     summary: Sort data by light in decreasing order
 *     description: Retrieves data sensor entries sorted by light in decreasing order, paginated with 50 records per page.
 *     parameters:
 *       - name: page
 *         in: query
 *         required: true
 *         description: Page number.
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
router.get('/sortLight/decrease', sortDecreaseLight);

/**
 * @swagger
 * /datasensor/sortTime/increase:
 *   get:
 *     summary: Sort data by time in increasing order
 *     description: Retrieves data sensor entries sorted by time in increasing order, paginated with 50 records per page.
 *     parameters:
 *       - name: page
 *         in: query
 *         required: true
 *         description: Page number.
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
router.get('/sortTime/increase', sortIncreaseTime);

/**
 * @swagger
 * /datasensor/sortTime/decrease:
 *   get:
 *     summary: Sort data by time in decreasing order
 *     description: Retrieves data sensor entries sorted by time in decreasing order, paginated with 50 records per page.
 *     parameters:
 *       - name: page
 *         in: query
 *         required: true
 *         description: Page number.
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
router.get('/sortTime/decrease', sortDecreaseTime);

router.get('/getTen', getTenDataSensor);

export default router;