document.addEventListener("DOMContentLoaded", function() {
    const recordButton = document.getElementById("recordButton");
    const stopButton = document.getElementById("stopButton");
    const pauseButton = document.getElementById("pauseButton");


    let mediaRecorder;
    let audioChunks = [];

    recordButton.addEventListener("click", startRecording);
    pauseButton.addEventListener("click", pauseRecording);
    stopButton.addEventListener("click", stopRecording);


    function startRecording() {
        recordButton.disabled = true;
        pauseButton.disabled = false;
        stopButton.disabled = false;

        audioChunks = [];

        navigator.mediaDevices.getUserMedia({ audio: true })
            .then(function(stream) {
                mediaRecorder = new MediaRecorder(stream);
                mediaRecorder.ondataavailable = event => {
                    if (event.data.size > 0) {
                        audioChunks.push(event.data);
                    }
                };
                mediaRecorder.start();
            })
            .catch(function(error) {
                console.error("Error accessing microphone:", error);
            });
    }

    function stopRecording() {
        recordButton.disabled = false;
        pauseButton.disabled = true;
        stopButton.disabled = true;

        if (mediaRecorder.state === "recording") {
            mediaRecorder.stop();
        }

        mediaRecorder.onstop = () => {
            const audioBlob = new Blob(audioChunks, { type: "audio/webm" });
            const formData = new FormData();
            formData.append("audio", audioBlob, "audio.webm");

            fetch("http://localhost:8080/api/v1/speechtotext/categorize", {
                method: "POST",
                body: formData
            })
            .then(response => response.json())
            .then(data => {
                const complaints = data.categories.complaints;
                const diagnosis = data.categories.diagnoses;
                const notes = data.categories.notes;
                populateDiv("complaints", complaints);
                populateDiv("diagnosis", diagnosis);
                populateDiv("notes", notes);
            })
            .catch(error => {
                console.error("Error uploading audio:", error);
            });
        };
    }

    function pauseRecording() {
        recordButton.disabled = false;
        pauseButton.disabled = true;
        stopButton.disabled = false;

        if (mediaRecorder.state === "recording") {
            mediaRecorder.stop();
        }
        
        mediaRecorder.onstop = () => {
            const audioBlob = new Blob(audioChunks, { type: "audio/webm" });
            const formData = new FormData();
            formData.append("audio", audioBlob, "audio.webm");

            fetch("http://localhost:8080/api/v1/speechtotext/categorize", {
                method: "POST",
                body: formData
            })
            .then(response => response.json())
            .then(data => {
                const complaints = data.categories.complaints;
                const diagnosis = data.categories.diagnoses;
                const notes = data.categories.notes;
                populateDiv("complaints", complaints);
                populateDiv("diagnosis", diagnosis);
                populateDiv("notes", notes);
            })
            .catch(error => {
                console.error("Error uploading audio:", error);
            });
        };

    }

    function populateDiv(divId, data) {
        const div = document.getElementById(divId);
        div.value += data.map(item => `${item}`).join(" ");
    }
});
