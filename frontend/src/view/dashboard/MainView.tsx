import { Button } from "../../components/commonComponent/Button";
import { Title } from "../../components/commonComponent/Title";
import { UpperBox, Box, UnderBox } from "../../components/dashboard/Body";
import { TitleContainer, Table, Th, Thead, Tbody, Tr, TdComponent, TdText, ExecButtonContainer, StopButton } from "../../components/dashboard/Main";
import { MdDelete } from "react-icons/md";
import Post from "../../Request/Post";

type DevleopmentEnv = {
    key: number,
    id: string,
    userId: string,
    name: string,
    status: string
  };
interface CreateDevelopViewProps {
    list: DevleopmentEnv[]
}
export default function MainView(props:CreateDevelopViewProps) {

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
                        props.list.map( (item:DevleopmentEnv) =>
                            (
                                <Tr>
                                    <TdText>{item.userId}</TdText>
                                    <TdText>{item.name}</TdText>
                                    <TdComponent>
                                        <ExecButtonContainer>
                                            {
                                            item.status==="Running" ?
                                                <StopButton onClick={()=>{
                                                    Post(`/dev/stop?id=${item.id}`,null)
                                                    .then(res=>{
                                                        window.location.reload();
                                                    })
                                                    .catch(err=>{
                                                        console.error(err);
                                                        alert("종료 실패");
                                                        window.location.reload();
                                                    })
                                                }}>
                                                    stop
                                                </StopButton> :
                                                <Button onClick={()=>{
                                                    const uri:string = `https://dev.seongho9.me/${item.userId}/${item.name.split("-")[1]}`
                                                    Post(`/dev/start?id=${item.id}`,null)
                                                    .then(res=>{
                                                        setTimeout(()=>{
                                                            window.open(uri);
                                                            window.location.reload();
                                                        }, 1000);
                                                    })
                                                    .catch(err=>{
                                                        console.error(err);
                                                        alert("시작 실패");
                                                        window.location.reload();
                                                    })
                                                }}>
                                                    start
                                                </Button>
                                                }
                                        </ExecButtonContainer>
                                    </TdComponent>
                                    <TdComponent>
                                        <MdDelete />
                                    </TdComponent>
                                </Tr>
                            ))
                    }
                </Tbody>
            </Table>
            {
                props.list.length === 0 ? 
                   <p>There is any Environments.</p> :
                    null
            }
        </UpperBox>
        <UnderBox />
    </Box>
  )
}
