import React, { Component } from 'react';
import CursaTable from './CursaTable.jsx';
import './App.css';
import CursaForm from "./CursaForm.jsx";
import { GetCurse, DeleteCursa, AddCursa, UpdateCursa } from './utils/rest-calls';

class CursaApp extends Component {
    constructor(props) {
        super(props);
        this.state = {
            curse: [{ "id": "167", "destinatie": "Maldive", "data": "12.12.2024", "ora": "12:00", "nrLocuri": "200" }]
        };
        console.log('CursaApp constructor');
    }

    addFunc = (cursa) => {
        console.log('inside add Func', cursa);
        AddCursa(cursa)
            .then(res => GetCurse())
            .then(curse => this.setState({ curse }))
            .catch(error => console.log('eroare add ', error));
    }

    updateFunc = (cursa) => {
        console.log('inside update Func', cursa);
        UpdateCursa(cursa)
            .then(res => GetCurse())
            .then(curse => this.setState({ curse }))
            .catch(error => console.log('eroare update ', error));
    }

    deleteFunc = (cursaId) => {
        console.log('inside deleteFunc', cursaId);
        DeleteCursa(cursaId)
            .then(res => GetCurse())
            .then(curse => this.setState({ curse }))
            .catch(error => console.log('eroare delete', error));
    }

    componentDidMount() {
        console.log('inside componentDidMount');
        GetCurse().then(curse => this.setState({ curse }));
    }

    render() {
        return (
            <div className="App">
                <h1> New Curse Management App </h1>
                <CursaForm addFunc={this.addFunc} updateFunc={this.updateFunc} />
                <br />
                <br />
                <CursaTable curse={this.state.curse} deleteFunc={this.deleteFunc} />
            </div>
        );
    }
}

export default CursaApp;
