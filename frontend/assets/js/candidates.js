document.addEventListener("DOMContentLoaded", () => {
    loadCandidates();
});

function loadCandidates() {
    fetch(`${BASE_URL}/api/candidate`, {
        headers: getAuthHeaders()
    })
    .then((res) => res.json())
    .then((data) => {
        let candidatesContainer = document.getElementById("candidatesContainer");
        candidatesContainer.innerHTML = "";
        data.forEach((candidate) => {
            candidatesContainer.innerHTML += `
                <tr>
                    <td>${candidate.id}</td>
                    <td>${candidate.name}</td>
                    <td>${candidate.party}</td>
                    <td>
                        <button class="btn btn-primary btn-sm" onclick="editCandidate(${candidate.id})">Edit</button>
                        <button class="btn btn-danger btn-sm" onclick="deleteCandidate(${candidate.id})">Delete</button>
                    </td>
                </tr>
            `;
        });
    })
    .catch(() => showToast("Error loading candidates.", "error"));
}

document.getElementById("candidateForm")
    ?.addEventListener("submit", function (event) {
        event.preventDefault();
        const candidateData = {
            name: document.getElementById("candidateName").value,
            party: document.getElementById("party").value,
        };

        fetch(`${BASE_URL}/api/candidate/add`, {
            method: "POST",
            headers: getAuthHeaders(),
            body: JSON.stringify(candidateData),
        })
        .then(() => {
            showToast("Candidate added successfully!", "success");
            document.getElementById("candidateForm").reset();
            loadCandidates();
        })
        .catch(() => showToast("Failed to add candidate.", "error"));
    });

function editCandidate(candidateId) {
    Swal.fire({
        title: "Edit Candidate Details",
        html: `<input type="text" id="newName" class="swal2-input" placeholder="Enter new name">
               <input type="text" id="newParty" class="swal2-input" placeholder="Enter new party">`,
        showCancelButton: true,
        confirmButtonText: "Update",
        preConfirm: () => {
            let newName = document.getElementById("newName").value;
            let newParty = document.getElementById("newParty").value;
            if (!newName || !newParty) {
                Swal.showValidationMessage("Both fields are required!");
                return false;
            }
            return { newName, newParty };
        },
    }).then((result) => {
        if (result.isConfirmed) {
            updateCandidate(candidateId, result.value.newName, result.value.newParty);
        }
    });
}

function updateCandidate(candidateId, newName, newParty) {
    fetch(`${BASE_URL}/api/candidate/update/${candidateId}`, {
        method: "PUT",
        headers: getAuthHeaders(),
        body: JSON.stringify({ name: newName, party: newParty }),
    })
    .then(() => {
        showToast("Candidate updated successfully!", "success");
        loadCandidates();
    })
    .catch(() => showToast("Failed to update candidate.", "error"));
}

function deleteCandidate(id) {
    Swal.fire({
        title: "Are you sure?",
        text: "This candidate will be permanently deleted.",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#d9534f",
        cancelButtonColor: "#6c757d",
        confirmButtonText: "Yes, delete it!",
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`${BASE_URL}/api/candidate/delete/${id}`, {
                method: "DELETE",
                headers: getAuthHeaders(),
            })
            .then(() => {
                showToast("Candidate deleted!", "success");
                loadCandidates();
            })
            .catch(() => showToast("Failed to delete candidate.", "error"));
        }
    });
}