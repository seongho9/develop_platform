import styled from "styled-components";

export const Header = styled.div<{ isOpen: boolean}>`
    display:flex;
    flex-direction: row;
    justify-content: flex-start;
    align-items: flex-start;

    background-color: #1976D2;
    color: white;

    width: 100vw;
    
    padding: 0;
    margin: 0;
    border: 0px;

    
    box-shadow: 4px 4px 4px 4px rgba(0,0,0,0.1);
`;
export const Menu =  styled.div<{ isOpen: boolean}>`
    ${props=>
        !props.isOpen &&
        `
            width: 2rem;
            color: white;
            background-color: #1976D2;
            
            padding-left: 0.5rem;
            padding-right: 1.0rem;
            font-size: 1.2rem;
        `
    }
    ${props=>
        props.isOpen &&
        `
            width: 2rem;
            color: white;
            background-color: #1976D2;
            
            font-size: 1.5rem;
            padding-left: 0.5rem;
            padding-right: 1.0rem;
            font-size: 1.2rem;
        `
    }
    
    padding-top:1.0rem;
    padding-bottom: 1.1rem;
    margin: 0;
`
export const Logout = styled.div`
    width: 2rem;
    padding: 0;
    margin: 0;
`;
export const TitleContainer = styled.div`
    pointer-events: none;
    flex-grow: 1;
    padding:0;
    margin: 0;
    font-size: 1.2rem;
    padding-bottom: 1rem;
    padding-top:0.8rem;
`;