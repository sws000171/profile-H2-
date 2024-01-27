function createList(){

  let section = document.getElementById("list");
  let store ="";
  
  console.log(member);

  store = `<div class="member">`;
  member.forEach(function(element){
    store += ` 
    <figure class="snip1336">
    <img src="${element.backPath}" alt="backimg" />
    <figcaption>
      <img src="${element.facePath}" alt="faceimg" class="profile" />
      <h2>${element.name}<span>${element.occupation}</span></h2>
      <p>${element.memo}</p>
      <a href="mailto:${element.email}" class="email">e-mail</a>
    `;

    if (element.other == ""){
      store += `<a href="#" class="info" >More Info</a>`
    }else{
      store += `<a href="${element.other}" class="info" target="_blank" rel="noopener noreferrer">More Info</a>`
    };
    
    store += `
    </figcaption>
    </figure>
    `;
  });
  store += `</div>`;
  section.innerHTML = store;
}
