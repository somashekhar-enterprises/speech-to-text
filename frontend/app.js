// Global variables
let mediaRecorder;
let recordedChunks = [];
let isRecording = false;

// Elements
const startButton = document.getElementById('startRecording');
const stopButton = document.getElementById('stopRecording');
const responseDiv = document.getElementById('response');

// Event listeners
startButton.addEventListener('click', startRecording);
stopButton.addEventListener('click', stopRecording);

// Function to start recording
function startRecording() {
    isRecording = true;
    startButton.disabled = true;
    stopButton.disabled = false;

    recordedChunks = [];
    navigator.mediaDevices.getUserMedia({ audio: true })
        .then(stream => {
            mediaRecorder = new MediaRecorder(stream);

            mediaRecorder.ondataavailable = event => {
                if (event.data.size > 0) {
                    recordedChunks.push(event.data);
                }
            };

            mediaRecorder.onstop = () => {
                isRecording = false;
                startButton.disabled = false;
                stopButton.disabled = true;

                // When recording stops, send the audio to the server
                sendToServer();
            };

            mediaRecorder.start();
        })
        .catch(error => {
            console.error('Error accessing the microphone:', error);
        });
}

// Function to stop recording
function stopRecording() {
    if (mediaRecorder && isRecording) {
        mediaRecorder.stop();
    }
}

// Function to send the recorded voice to the server and stream the response
async function sendToServer() {
    const audioBlob = new Blob(recordedChunks, { type: 'audio/webm' });
    const formData = new FormData();
    formData.append('audio', audioBlob);

    try {
        const response = await fetch('http://127.0.0.1:8080/api/v1/speechtotext/stream/transcribe', {
            method: 'POST',
            body: formData
        });

        // Get the response body as a ReadableStream
        const reader = response.body.getReader();

        // Function to continuously read and display streamed data
        const readStream = async () => {
            const { done, value } = await reader.read();

            if (done) {
                // Streamed response complete
                return;
            }

            // Decode the streamed data and display on the screen
            const text = new TextDecoder().decode(value);
            responseDiv.textContent += text;

            // Continue reading the stream
            readStream();
        };

        // Start reading the stream
        readStream();
    } catch (error) {
        console.error('Error sending audio to server:', error);
    }
}
