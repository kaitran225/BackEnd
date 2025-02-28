    let link = document.createElement("link");
    link.rel = "stylesheet";
    link.href = window.location.origin + "/style.css"; // Dynamically sets the domain
    document.head.appendChild(link);
