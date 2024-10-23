import swaggerJSDoc from 'swagger-jsdoc'

const swaggerOptions = {
  swaggerDefinition: {
    openapi: '3.0.0',
    info: {
      title: 'API Documentation',
      version: '1.0.0',
      description: 'API documentation for IoTApp',
    },
    servers: [
      {
        url: 'http://localhost:8000',
      },
    ],
  },
  apis: ['./routes/*.js'],
};

export default swaggerJSDoc(swaggerOptions);