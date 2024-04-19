const typed = new Typed('.multiple',
{
    strings: ['Front-End Developer Jr','Back-End Developer Jr','Estudiante De MAC'],
    typeSpeed:50,
    backSpeed:50,
    backDelay:1000,
    loop:true
});

document.querySelector('.skill').addEventListener('click', function() 
{
    var leftContent = document.querySelector('.leftS'); 
    var contenidoS = document.getElementById('contenidoS');
  
    if (contenidoS.style.display === 'none') 
    {
      contenidoS.style.display = 'grid'; 
      leftContent.style.display = 'none';
      this.textContent = 'Informacion';
    } else 
    {
      contenidoS.style.display = 'none'; 
      leftContent.style.display = 'block'; 
      this.textContent = 'Skills';
    }
  });
  