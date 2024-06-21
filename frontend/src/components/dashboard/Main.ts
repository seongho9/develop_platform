import styled from "styled-components";


export const Table = styled.table`
    border-spacing: 0px;
    text-align: left;
    padding-top: 0.5rem;
    padding-bottom: 0.5rem;
    padding-left: 1rem;
    padding-right: 1rem;
    margin: 0;
`;

export const Thead = styled.thead`
    padding: 0;
    margin: 0;
    height: 3rem;
`

export const Tbody = styled.tbody`
    padding: 0;
    margin: 0;
`;

export const Tr = styled.tr`

    padding:0;
    margin: 0;
`;

export const Th = styled.th`
    border-bottom: 1px solid rgb(200,200,200);
    padding-left: 1rem;
    padding-right: 1rem;
    margin: 0;
`;
export const TdText = styled.td`
    border-bottom: 1px solid rgb(200,200,200);
    padding-left: 1rem;
    padding-right: 1rem;
    padding-top: 1.5rem;
    padding-bottom: 1.5rem;
    margin: 0;
`
export const TdComponent = styled.td`
    border: 0px;
    border-bottom: 1px solid rgb(200,200,200);
    padding:0;
    margin: 0;
`;

export const TitleContainer = styled.div`
    font-size: 1.5rem;
    color: #1976D2;

    white-space: nowrap;
    overflow: hidden;
    padding-top: 1.5rem;
    padding-bottom: 1.5rem;
`
export const StopButton = styled.button`
    width: 100%;
    padding: 0.75rem;

    border: none;
    border-radius: 8px;


    font-size: 1rem;
    cursor: pointer;
    background-color: #ff0000;
    color: white;

    &:hover {
        background-color: #dd0000;
    }
`;
export const ExecButtonContainer = styled.div`
    width: 8rem;
    padding-top: 0.5rem;
    padding-bottom: 0.5rem;
`;

export const DeleteContainer = styled.div`
    padding:0;
    margin:0;
    width:1rem;
    &:hover{
        color: gray;
        cursor: pointer;
    }
`