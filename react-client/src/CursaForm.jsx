import React, { useState, useEffect } from 'react';

export default function CursaForm({ addFunc, updateFunc, selectedCursa, setSelectedCursa }) {
    const [cursa, setCursa] = useState({ destinatie: '', data: '', ora: '', nrLocuri: '' });

    useEffect(() => {
        if (selectedCursa) {
            setCursa(selectedCursa);
        }
    }, [selectedCursa]);

    function handleChange(event) {
        const { name, value } = event.target;
        setCursa({ ...cursa, [name]: value });
    }

    function handleSubmit(event) {
        event.preventDefault();
        if (selectedCursa) {
            console.log('Submitting update:', cursa);
            updateFunc(cursa);
            setSelectedCursa(null);
        } else {
            console.log('Submitting add:', cursa);
            addFunc(cursa);
        }
        setCursa({ destinatie: '', data: '', ora: '', nrLocuri: '' });
    }

    return (
        <form onSubmit={handleSubmit}>
            <label>
                Destinatie:
                <input type="text" name="destinatie" value={cursa.destinatie} onChange={handleChange} />
            </label>
            <br />
            <label>
                Data:
                <input type="text" name="data" value={cursa.data} onChange={handleChange} />
            </label>
            <br />
            <label>
                Ora:
                <input type="text" name="ora" value={cursa.ora} onChange={handleChange} />
            </label>
            <br />
            <label>
                Numar locuri:
                <input type="text" name="nrLocuri" value={cursa.nrLocuri} onChange={handleChange} />
            </label>
            <br />
            <button type="submit">{selectedCursa ? 'Update' : 'Add'}</button>
        </form>
    );
}
