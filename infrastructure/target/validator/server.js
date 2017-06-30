const express = require('express');
const path = require('path');
const exec = require('child_process').exec;
const fs = require('fs');

const app = express();
app.set('view engine', 'ejs');

app.get("/", (req, res) => {
    let applicationHome=path.join(__dirname, '..', 'application')

    exec(`./run.sh`, {cwd: applicationHome}, (err, stdout, stderr) => {
        let status="ok";
        status="";

        if (err) {
            console.error(err);
            status+=err;
        } else {
            status=stdout;
        }

        fs.readFile(`${applicationHome}/appOutput.json`, { encoding: 'utf8' }, (err, data) => {
            if (err) {
                console.error(err);
                status+=`\n\n${err}`;
            }
            
            res.render('dashboard', {
                status: status,
                output: data
            });
        });
    })
});

app.listen(process.env.PORT || 8080, () => {
  console.log(`Your app is listening on port ${process.env.PORT || 8080}`);
});