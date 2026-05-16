document.addEventListener("DOMContentLoaded", () => {
    loadCandidates();
    loadVotes();
});

// Load candidates for voting
function loadCandidates() {
    fetch(`${BASE_URL}/api/candidate`, {
        headers: getAuthHeaders()
    })
    .then((res) => res.json())
    .then((data) => {
        let candidateSelect = document.getElementById("candidateSelect");
        candidateSelect.innerHTML = '<option value="">Select a Candidate</option>';
        data.forEach((candidate) => {
            let option = document.createElement("option");
            option.value = candidate.id;
            option.textContent = `${candidate.name} (${candidate.party})`;
            candidateSelect.appendChild(option);
        });
    })
    .catch(() => showAlert("Error", "Failed to load candidates.", "error"));
}

// Load votes
function loadVotes() {
    fetch(`${BASE_URL}/api/votes`, {
        headers: getAuthHeaders()
    })
    .then((res) => res.json())
    .then((data) => {
        let votesTable = document.getElementById("votesTable");
        votesTable.innerHTML = "";
        data.forEach((vote) => {
            votesTable.innerHTML += `<tr>
                <td>${vote.voterId}</td>
                <td>${vote.candidateId}</td>
            </tr>`;
        });
    })
    .catch(() => showAlert("Error", "Failed to load votes.", "error"));
}

// Cast vote
function castVote() {
    const voterId = document.getElementById("voterId").value;
    const candidateId = document.getElementById("candidateSelect").value;
    const electionName = document.getElementById("electionName").value;

    if (!voterId || !candidateId || !electionName) {
        showAlert("Warning", "Please fill all fields.", "warning");
        return;
    }

    fetch(`${BASE_URL}/api/votes/cast`, {
        method: "POST",
        headers: getAuthHeaders(),
        body: JSON.stringify({ voterId, candidateId, electionName })
    })
    .then((response) => {
        if (!response.ok) {
            return response.json().then((err) => {
                throw new Error(err.messageString || "Something went wrong!");
            });
        }
        return response.json();
    })
    .then((obj) => {
        showAlert("Success", obj.message, "success");
        document.getElementById("voteForm") && document.getElementById("voteForm").reset();
        loadVotes();
    })
    .catch((error) => {
        showAlert("Error", error.message, "error");
    });
}