import express from 'express';
import { createActionHistory, getTimeBySearch, getActionHistory } from '../controllers/ActionHistoryController.js';

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
router.post('/', createActionHistory);

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
 *         description: The time to search for .
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
 * /actionhistory:
 *   get:
 *     summary: Get action history
 *     description: Returns a list of action history for devices, paginated with 50 records per page.
 *     parameters:
 *       - name: page
 *         in: query
 *         required: true
 *         description: Page number
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
router.get('/', getActionHistory);

export default router;