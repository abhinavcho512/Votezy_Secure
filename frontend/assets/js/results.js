document.getElementById("searchResultForm")
    .addEventListener("submit", function (event) {
        event.preventDefault();

        const electionName = document.getElementById("electionName").value;
        const requestData = { electionName: electionName };

        fetch(`${BASE_URL}/api/election-result/declare`, {
            method: "POST",
            headers: getAuthHeaders(),
            body: JSON.stringify(requestData),
        })
        .then((res) => res.json())
        .then((data) => {
            const electionNameVal = data.electionName || "N/A";
            const totalVotes = data.totalVotes || 0;
            const winnerId = data.winnerId || "N/A";
            const winnerVotes = data.winnerVotes || 0;

            // ✅ Fetch winner name using winnerId
            fetch(`${BASE_URL}/api/candidate/${winnerId}`, {
                headers: getAuthHeaders()
            })
            .then(res => res.json())
            .then(candidate => {
                let resultHTML = `
                    <div class="card text-center shadow-lg p-4" style="width: 400px;">
                        <h4 class="text-danger">${electionNameVal}</h4>
                        <p><strong>Total Votes:</strong> ${totalVotes}</p>
                        <p><strong>🏆 Winner:</strong> ${candidate.name}</p>
                        <p><strong>Party:</strong> ${candidate.party}</p>
                        <p><strong>Votes Obtained:</strong> ${winnerVotes}</p>
                    </div>
                `;
                document.getElementById("resultsContainer").innerHTML = resultHTML;
            })
            .catch(() => {
                // fallback if candidate fetch fails
                let resultHTML = `
                    <div class="card text-center shadow-lg p-4" style="width: 400px;">
                        <h4 class="text-danger">${electionNameVal}</h4>
                        <p><strong>Total Votes:</strong> ${totalVotes}</p>
                        <p><strong>Winner ID:</strong> ${winnerId}</p>
                        <p><strong>Votes Obtained:</strong> ${winnerVotes}</p>
                    </div>
                `;
                document.getElementById("resultsContainer").innerHTML = resultHTML;
            });
        })
        .catch((error) => {
            console.log(error);
            showAlert("Error", "Election result not found.", "error");
        });
    });