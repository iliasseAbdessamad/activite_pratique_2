const inputFileContainer = document.getElementById("image");
const inputFile = inputFileContainer.children[0];

inputFileContainer.style.width = "200px";
inputFileContainer.style.height = "200px";
inputFileContainer.style.border = ".2px solid grey";
inputFileContainer.style.borderRadius = "7px";


if(window.location.href.endsWith("/new")){
    inputFileContainer.style.backgroundImage = "url('/images/default.png')";
}
inputFileContainer.style.backgroundSize = "cover";
inputFileContainer.style.backgroundPosition = "center";

inputFile.style.opacity = "0";
inputFile.style.width = inputFileContainer.style.width;
inputFile.style.height = inputFileContainer.style.height;
inputFile.style.cursor = "pointer";

inputFile.addEventListener("change", (event) => {
    const file = event.target.files[0];
    console.log(file);
    if (file) {
        console.log("yes");
        const imageURL = URL.createObjectURL(file);
        inputFileContainer.style.backgroundImage = `url(${imageURL})`;
    }
});
