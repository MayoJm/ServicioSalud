function showSection(sectionId) {
    
    document.querySelectorAll('section').forEach(function (section) {
        section.style.display = 'none';
    });

    
    document.getElementById(sectionId).style.display = 'block';
}
function toggleDropdown() {
    var dropdown = document.getElementById("profile-dropdown");
    dropdown.style.display = (dropdown.style.display === "block") ? "none" : "block";
}

function showProfileOptions() {
    
    var options = document.getElementById('profile-options');
    options.style.display = 'block';
}

function showAvatarOptions() {
    var avatarOptions = document.getElementById('avatar-options');
    avatarOptions.style.display = 'block';

    
    var homeSection = document.getElementById('home');
    homeSection.style.display = 'none';
}

function selectAvatar(avatarSrc) {
    var profilePicture = document.getElementById('profile-picture');
    profilePicture.src = avatarSrc;

    
    var avatarOptions = document.getElementById('avatar-options');
    avatarOptions.style.display = 'none';

    var homeSection = document.getElementById('home');
    homeSection.style.display = 'block';
}


