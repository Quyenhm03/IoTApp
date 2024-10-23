import express from 'express';
import { createActionHistory, getAllHistory, getTimeBySearch, warningHigh } from '../controllers/ActionHistoryController.js';

const router = express.Router();

/**
 * @swagger
 * /actionhistory:
 *   post:
 *     summary: Create action history
 *     description: Creates a new action history entry.
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               device:
 *                 type: string
 *               action:
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
router.post('/', createActionHistory);

/**
 * @swagger
 * /actionhistory/warningHigh:
 *   post:
 *     summary: Send warning message
 *     description: Sends a warning message to MQTT.
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               message:
 *                 type: string
 *     responses:
 *       200:
 *         description: Message sent successfully
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
 *                   type: string
 *       500:
 *         description: Failed to send message
 */
router.post('/warningHigh', warningHigh);

/**
 * @swagger
 * /actionhistory:
 *   get:
 *     summary: Get all action histories
 *     description: Retrieves all action history entries.
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
router.get('/', getAllHistory);

/**
 * @swagger
 * /actionhistory/searchTime:
 *   get:
 *     summary: Search action histories by time
 *     description: Retrieves action histories based on the provided time.
 *     parameters:
 *       - name: time
 *         in: query
 *         required: true
 *         description: The time to search for (can be full date, date, or time).
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

export default router;