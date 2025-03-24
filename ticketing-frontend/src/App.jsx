import { useState, useEffect } from 'react';
import axios from 'axios';
import './App.css'; // Importing CSS for styling

const App = () => {
    const [config, setConfig] = useState({
        numberOfVendors: '',
        totalTickets: '',
        ticketReleaseRate: '',
        numberOfCustomers: '',
        customerRetrievalRate: '',
        maxTicketCapacity: '',
        numberOfVIPCustomers: '',
        ticketsPerVendor: '',
        ticketsPerCustomer: ''
    });

    const [configs, setConfigs] = useState([]);

    useEffect(() => {
        axios
            .get('http://localhost:8080/api/configs/getAll')
            .then((response) => {
                if (Array.isArray(response.data)) {
                    setConfigs(response.data);
                } else {
                    console.error('Expected an array, but got:', response.data);
                }
            })
            .catch((error) => console.error('Error fetching configurations', error));
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        if (value === '' || /^[1-9][0-9]*$/.test(value)) {
            setConfig((prevConfig) => ({
                ...prevConfig,
                [name]: value
            }));
        }
    };

    const handleSave = (e) => {
        e.preventDefault();
        axios
            .post('http://localhost:8080/api/configs/save', config)
            .then((response) => {
                const savedConfig = response.data;
                alert('Configuration saved successfully!');

                setConfigs([...configs, savedConfig]);
                setConfig({
                    numberOfVendors: '',
                    totalTickets: '',
                    ticketReleaseRate: '',
                    numberOfCustomers: '',
                    customerRetrievalRate: '',
                    maxTicketCapacity: '',
                    numberOfVIPCustomers: '',
                    ticketsPerVendor: '',
                    ticketsPerCustomer: ''
                });

                // Start the configuration using the CLI API
                startConfiguration(savedConfig.id);
            })
            .catch((error) => console.error('Error saving configuration', error));
    };

    const startConfiguration = (configId) => {
        axios
            .post(`http://localhost:8080/api/system/start?id=${configId}`)
            .then((response) => {
                alert(response.data); // Show the response from the server
            })
            .catch((error) => {
                if (error.response) {
                    alert(`Error: ${error.response.data}`);
                } else {
                    console.error('Unexpected error', error);
                    alert('An unexpected error occurred. Please try again.');
                }
            });
    };
    const handleDelete = (id) => {
        if (window.confirm('Are you sure you want to delete this configuration?')) {
            axios
                .delete(`http://localhost:8080/api/configs/delete?id=${id}`)
                .then(() => {
                    alert('Configuration deleted successfully!');
                    setConfigs(configs.filter((config) => config.id !== id));
                })
                .catch((error) => console.error('Error deleting configuration', error));
        }
    };

    return (
        <div className="app-container">
            <h1 className="header">Configuration Dashboard</h1>

            {/* Form for adding new configuration */}
            <form className="config-form" onSubmit={handleSave}>
                <h2>Add New Configuration</h2>
                {[
                    {label: 'Number of Vendors', name: 'numberOfVendors'},
                    {label: 'Total Tickets', name: 'totalTickets'},
                    {label: 'Ticket Release Rate', name: 'ticketReleaseRate'},
                    {label: 'Number of Customers', name: 'numberOfCustomers'},
                    {label: 'Customer Retrieval Rate', name: 'customerRetrievalRate'},
                    {label: 'Max Ticket Capacity', name: 'maxTicketCapacity'},
                    {label: 'Number of VIP Customers', name: 'numberOfVIPCustomers'},
                    {label: 'Tickets Per Vendor', name: 'ticketsPerVendor'},
                    {label: 'Tickets Per Customer', name: 'ticketsPerCustomer'}
                ].map(({label, name}) => (
                    <div className="form-group" key={name}>
                        <label>{label}</label>
                        <input
                            type="number"
                            name={name}
                            value={config[name]}
                            onChange={handleChange}
                            required
                        />
                    </div>
                ))}
                <button type="submit" className="submit-btn">
                    Save Configuration
                </button>
            </form>

            {/* List all configurations */}
            <h2>All Configurations</h2>
            <ul className="config-list">
                {Array.isArray(configs) && configs.length > 0 ? (
                    configs.map((config) => (
                        <li key={config.id} className="config-item">
                            <div className="config-details">
                                <p><strong>ID:</strong> {config.id}</p>
                                <p><strong>Number of Vendors:</strong> {config.numberOfVendors}</p>
                                <p><strong>Total Tickets:</strong> {config.totalTickets}</p>
                                <p><strong>Ticket Release Rate:</strong> {config.ticketReleaseRate}</p>
                                <p><strong>Number of Customers:</strong> {config.numberOfCustomers}</p>
                                <p><strong>Customer Retrieval Rate:</strong> {config.customerRetrievalRate}</p>
                                <p><strong>Max Ticket Capacity:</strong> {config.maxTicketCapacity}</p>
                                <p><strong>Number of VIP Customers:</strong> {config.numberOfVIPCustomers}</p>
                                <p><strong>Tickets Per Vendor:</strong> {config.ticketsPerVendor}</p>
                                <p><strong>Tickets Per Customer:</strong> {config.ticketsPerCustomer}</p>

                                {/* Delete button */}
                                <button onClick={() => handleDelete(config.id)} className="delete-btn">Delete</button>

                                {/* Start Simulation button */}
                                <button
                                    onClick={() => startConfiguration(config.id)}
                                    className="start-simulation-btn"
                                >
                                    Start Simulation
                                </button>
                            </div>
                        </li>
                    ))
                ) : (
                    <p>No configurations available.</p>
                )}
            </ul>
        </div>
    );
};

export default App;