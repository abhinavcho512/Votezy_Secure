document.addEventListener("DOMContentLoaded", () => {
    loadVoters();
});

// Load all voters
function loadVoters() {
    fetch(`${BASE_URL}/api/voters`, {
        headers: getAuthHeaders()
    })
    .then((res) => res.json())
    .then((data) => {
        let votersContainer = document.getElementById("votersContainer");
        votersContainer.innerHTML = "";
        data.forEach((voter) => {
            votersContainer.innerHTML += `
                <tr>
                    <td>${voter.id}</td>
                    <td>${voter.name}</td>
                    <td>${voter.email}</td>
                    <td><button class="btn btn-danger" onclick="deleteVoter(${voter.id})">Delete</button></td>
                </tr>
            `;
        });
    })
    .catch((error) => {
        console.log(error);
        showAlert("Error", "Failed to load voters.", "error");
    });
}

// Register voter
document.getElementById("voterForm")
    ?.addEventListener("submit", function (event) {
        event.preventDefault();
        const voterData = {
            name: document.getElementById("name").value,
            email: document.getElementById("email").value,
        };

        fetch(`${BASE_URL}/api/voters/register`, {
            method: "POST",
            headers: getAuthHeaders(),
            body: JSON.stringify(voterData),
        })
        .then(() => {
            showAlert("Success", "Voter registered successfully!", "success");
            document.getElementById("voterForm").reset();
            loadVoters();
        })
        .catch(() => showAlert("Error", "Voter registration failed.", "error"));
    });

// Delete voter
function deleteVoter(id) {
    Swal.fire({
        title: "Are you sure?",
        text: "This voter will be permanently deleted.",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#d9534f",
        cancelButtonColor: "#6c757d",
        confirmButtonText: "Yes, delete it!",
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`${BASE_URL}/api/voters/delete/${id}`, {
                method: "DELETE",
                headers: getAuthHeaders(),
            })
            .then(() => {
                showAlert("Deleted!", "Voter has been removed.", "success");
                loadVoters();
            })
            .catch(() => showAlert("Error", "Failed to delete voter.", "error"));
        }
    });
}