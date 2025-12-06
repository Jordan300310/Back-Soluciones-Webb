## Pasos para Usar la pasarela de pagos
######
1. Instalar NGrok , una vez instalado, abrir dos cmds y colocar los comandos  `ngrok http 8080 --pooling-enabled` y `ngrok http 4200 --pooling-enabled` 
2. Copiar el forwarding que te aparece y remplazarlo en el application.properties. en `mercadopago.succes.url` ,  `mercadopago.failure.url`,  `mercadopago.pending.url` y  `mercadopago.notification.url`
3. Usar cuenta de testeo si no tiene para completar la compra, 
Comprador User: TESTUSER9105054218021732994
Pass: k0SvrxrKXl
4. Usar codigo de verificacion para inciar sesion si lo pide 513214