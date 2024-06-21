import { useSearchParams } from "react-router-dom";
import DashBoradView from "../../view/DashBoardView";


export default function Dashboard() {
    
    const [params] = useSearchParams();
    const page = params.get("page");
    

    return (
        <DashBoradView page={page}/>
    );
}
