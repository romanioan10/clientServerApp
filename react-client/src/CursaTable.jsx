import React from 'react';
import './App.css';

function CursaRow({cursa, deleteFunc, updateFunc}) {
    function handleDelete() {
        console.log('Delete button clicked for', cursa.id);
        deleteFunc(cursa.id);
    }

    function handleUpdate() {
        console.log('Update button clicked for', cursa.id);
        updateFunc(cursa);
    }

    return (
        <tr>
            <td>{cursa.destinatie}</td>
            <td>{cursa.data}</td>
            <td>{cursa.ora}</td>
            <td>{cursa.nrLocuri}</td>
            <td>
                <button onClick={handleDelete}>Delete</button>
                <button onClick={handleUpdate}>Update</button>
            </td>
        </tr>
    );
}

export default function CursaTable({ curse, deleteFunc, updateFunc}) {
    console.log("Rendering CursaTable");
    console.log(curse);
    let rows = [];
    curse.forEach(function (cursa) {
        rows.push(<CursaRow cursa={cursa} key={cursa.id} deleteFunc={deleteFunc} updateFunc={updateFunc}/>);
    });
    return (
        <table>
            <thead>
            <tr>
                <th>Destinatie</th>
                <th>Data</th>
                <th>Ora</th>
                <th>Numar locuri</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>{rows}</tbody>
        </table>
    );
}
