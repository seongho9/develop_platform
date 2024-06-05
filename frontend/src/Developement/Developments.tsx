import * as React from 'react';
import { useEffect, useState } from 'react';
import Button from '@mui/material/Button';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import { Title } from '../components/commonComponent/Title';
import Get from '../Request/Get';
import { Clear } from '@mui/icons-material';
import Post from '../Request/Post';
import Delete from '../Request/Delete';

interface Container{
  id: string,
  userId: string,
  name: string, 
  running: string,
};
type DevleopmentEnv = {
  key: number,
  id: string,
  userId: string,
  name: string,
  status: string
};
export default function Developments() {
  
  const [containerList, setContainer] = useState<DevleopmentEnv[]>([]);
  

  useEffect( ()=>{
    const url:string = "/dev/list";
    Get(url)
      .then((data:Response)=>(data.json()))
      .then((json:Container[])=>{
        let count = 0;
        console.log(json);
        json.map((item: Container)=>
          setContainer(prev=>[
            ...prev,
            {
              key: count++,
              id: item.id,
              userId: item.userId,
              name: item.name,
              status: item.running ? "Running" : "NotRunning"
            }
          ])
        );
      })
      .catch(err=> alert(err));
  }, []);
  return (
    <React.Fragment>
      <Title>Development Environments</Title>
      <Table size="small">
        <TableHead>
          <TableRow>
            <TableCell>User</TableCell>
            <TableCell>Name</TableCell>
            <TableCell></TableCell>
            <TableCell sx={{padding:0, marginRight:0}}></TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {containerList.map((row) => (
            <TableRow key={row.key}>
              <TableCell>{row.userId}</TableCell>
              <TableCell>{row.name}</TableCell>
              <TableCell>
                {
                row.status==="Running" ? 
                <Button
                fullWidth
                variant="contained"
                sx={{ mt: 3, mb: 2, color:'white', backgroundColor:'red'}}
                onClick={()=>{
                  Post(`/dev/stop?id=${row.id}`, null)
                  .then(res=>res.text())
                  .then(data=>{
                    console.log(data);
                    window.location.reload();
                  })
                }}
                > 
                  Stop
                </Button>:
                <Button
                  fullWidth
                  variant="contained"
                  sx={{ mt: 3, mb: 2 }}
                  onClick={()=>{
                    const dev:string[] = row.name.split("-");
                    console.log(row.id);
              
                    Post(`/dev/start?id=${row.id}`, null)
                    .then(res=>res.text())
                    .then(data=>{
                      setTimeout(()=>{
                        window.open(`https://dev.seongho9.me/seongho9/${dev[1]}`);
                        window.location.reload();
                      }, 1000);
                      
                    })
                  }}
                > 
                  Start
                </Button>
                }
              </TableCell>
              <TableCell sx={{padding:0, marginRight:0, color:'red'}}>
                <Button sx={{color:'red'}}
                  onClick={()=>{
                    
                    const name:string = row.name;
                    name.substring(name.indexOf("-")+1);
                    const body={
                      id: row.id,
                      imageName: name
                    };
                    console.log(body);
                    Delete('/dev/delete', body)
                    .then(res=>{
                      alert("deleted");
                      window.location.reload();
                    })
                    .catch(err=>{
                      alert(err);
                      console.log(err);
                    });
                  }}>
                  <Clear />
                </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </React.Fragment>
  );
}