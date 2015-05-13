# Bubble Docs SD-STORE

# Projecto de Sistemas Distribuídos #

## Segunda entrega ##

Grupo de SD 44

Duarte Pompeu 73008 pnt12@live.com.pt


Repositorio:
[duarte-pompeu/A_31_33_44-project](https://github.com/duarte-pompeu/A_31_33_44-project)


-------------------------------------------------------------------------------

## Serviço SD SD-STORE 

### Instruções 
*(Como colocar o projecto a funcionar numa maquina do laboratorio)*

[0] Iniciar sistema operativo

Linux


[1] Iniciar servidores de apoio

JUDDI:
> startup.sh

[2] Criar pasta temporaria

> cd


[3] Obter versao entregue

> git clone https://github.com/duarte-pompeu/A_31_33_44-project.git sd-proj-aval2

> cd sd-proj-aval2

> git checkout tags/SD-STORE_R_2


[4] Construir e executar **servidor**

> cd sd-store/evaluation2

> chmod +x *.sh

> ./install.sh


[5] Construir **cliente**

> (já construido devido ao install.sh)

...


-------------------------------------------------------------------------------

### InstruÃ§Ãµes de teste: ###
*(Como verificar que todas as funcionalidades estÃ£o a funcionar correctamente)*


[1] Executar **cliente de testes** ...

> ./run_client.sh local


[2] Executar ...

> ./run_server.sh
(1x por cada servidor desejado, em terminais diferentes)

> ./run_client local_test

> ./run_client ws_test

> ./run_client fe_test



-------------------------------------------------------------------------------
**FIM**
