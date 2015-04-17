# Bubble Docs SD-ID
## Projecto de Sistemas Distribuídos ##

## Primeira entrega ##

Grupo de SD 33

Diogo Bastos	65675 diogo.bastos@tecnico.ulisboa.pt 

Tiago Nascimento 70493	ti_tyago@hotmail.com

Marcos Pires 	 72850	marcos.danix@gmail.com



Repositório:
[tecnico-softeng-distsys-2015/A_31_33_44-project](https://github.com/tecnico-softeng-distsys-2015/A_31_33_44-project/)


-------------------------------------------------------------------------------

## Serviço SD-ID

### Instruções de instalação 
*(Como colocar o projecto a funcionar numa máquina do laboratório)*

[0] Inicie um sistema operativo GNU/Linux


[1] Iniciar servidores de apoio

JUDDI:
> startup.sh

[2] Criar pasta temporária

> cd 
> mkdir avaliacao-sd

[3] Obter versão entregue

> git clone https://github.com/tecnico-softeng-distsys-2015/A_31_33_44-project.git sd-proj

> cd sd-proj

> git checkout tags/SD-ID_R_1



[4] Construir e executar **servidor**

> cd sd-id/evaluation

> chmod +x *.sh

> ./install.sh

> ./run_server.sh



[5] Construir **cliente**

> (já construido devido ao install.sh)


-------------------------------------------------------------------------------

### Instruções de teste: ###
*(Como verificar que todas as funcionalidades estão a funcionar correctamente)*


[1] Executar **cliente de testes** ...

> ./run_client.sh


[2] Executar ...



...


-------------------------------------------------------------------------------
**FIM**
