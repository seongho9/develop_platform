import { Button } from "../../components/commonComponent/Button";
import { Title } from "../../components/commonComponent/Title";
import { UpperBox, Box, UnderBox } from "../../components/dashboard/Body";
import { TitleContainer, Table, Th, Thead, Tbody, Tr, TdComponent, TdText, ExecButtonContainer, StopButton, DeleteContainer } from "../../components/dashboard/Main";
import { MdDelete } from "react-icons/md";

type DevleopmentEnv = {
    key: number,
    id: string,
    userId: string,
    name: string,
    status: string
  };
interface MainViewProps {
    list: DevleopmentEnv[];
    handleStop:(id:string)=>void;
    handleStart: (id:string, url:string)=>void;
    handleDelete: (id:string, image:string)=>void;
}
export default function MainView(props:MainViewProps) {

  return (
    <Box>
      <UpperBox>
        <TitleContainer>
          <Title> Development  Environment</Title>
        </TitleContainer>
        <Table>
          <Thead>
            <Th>User</Th>
            <Th>Name</Th>
            <Th></Th>
            <Th></Th>
          </Thead>
          <Tbody> 
          {
            props.list.map((item:DevleopmentEnv) =>
              (
                <Tr>
                  <TdText>{item.userId}</TdText>
                  <TdText>{item.name}</TdText>
                  <TdComponent>
                    <ExecButtonContainer>
                      {
                        item.status==="Running" ?
                          <StopButton onClick={()=>{props.handleStop(item.id)}}> 
                            stop
                          </StopButton> 
                            :
                          <Button onClick={()=>{
                            const uri:string = `https://dev.seongho9.me/${item.userId}/${item.name.split("-")[1]}`
                            props.handleStart(item.id, uri); 
                          }}> 
                            start
                          </Button>
                      }
                    </ExecButtonContainer>
                  </TdComponent>
                  <TdComponent>
                    <DeleteContainer>
                      <MdDelete onClick={()=>{
                        const name:string = item.name;
                        name.substring(name.indexOf("-")+1);
                        props.handleDelete(item.id, name);
                      }}/>
                    </DeleteContainer>
                  </TdComponent>
                </Tr>
              ))}
          </Tbody>
        </Table>
        {
          props.list.length === 0 ? 
            <p>There is any Environments.</p> :
            null
        }
      </UpperBox>
    <UnderBox />
  </Box>);
}
