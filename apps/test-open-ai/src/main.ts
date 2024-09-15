import OpenAI from 'openai';
import fs from 'fs';
import path from 'path';

console.log('Hello World');
console.log('folder Name', __dirname);
const filePath = path.resolve('TODO ADD PATH');
console.log('filePath', filePath);
fs.existsSync(filePath)
  ? console.log('file exists')
  : console.log('file does not exist');

const openai = new OpenAI({
  dangerouslyAllowBrowser: true,
  apiKey: 'TODO ADD API KEY',
});

// openai.files
//   .create({
//     file: fs.createReadStream(filePath),
//     purpose: 'assistants',
//   })
//   .then((response) => {
//     console.log('response', response);
//   });

// openai.chat.completions
//   .create({
//     model: 'gpt-4o',
//     messages: [{ role: 'user', content: 'write a haiku about ai' }],
//   })
//   .then((response) => {
//     console.log('response', response);
//   });
