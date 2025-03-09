const express = require('express');
const httpProxy = require('http-proxy');

const app = express();
const proxy = httpProxy.createProxyServer();

app.use((req, res) => {
  const token = req.query.token;
  if (token) {
    req.headers['authorization'] = `Bearer ${token}`;
    proxy.web(req, res, { target: 'http://localhost:7050' });
  } else {
    res.status(403).send('Bearer token missing');
  }
});

const port = 8000;
app.listen(port, () => {
  console.log(`Proxy server running at http://localhost:${port}`);
});
