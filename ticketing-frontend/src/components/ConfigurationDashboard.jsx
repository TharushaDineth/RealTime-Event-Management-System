// import React, { useState, useEffect } from 'react';
// import { Link } from 'react-router-dom';  // Import Link component from react-router-dom

// const ConfigurationDashboard = () => {
//   const [config, setConfig] = useState({
//     numberOfVendors: '',
//     totalTickets: '',
//     ticketReleaseRate: '',
//     numberOfCustomers: '',
//     customerRetrievalRate: '',
//     maxTicketCapacity: '',
//     numberOfVIPCustomers: '',
//     ticketsPerVendor: '',
//     ticketsPerCustomer: '',
//   });

//   const [configs, setConfigs] = useState([]);
//   const [loading, setLoading] = useState(true);

//   useEffect(() => {
//     const fetchConfigs = async () => {
//       try {
//         const response = await fetch('/api/configs/getAll');
//         const data = await response.json();
//         setConfigs(data);
//       } catch (error) {
//         console.error('Error fetching configurations:', error);
//       } finally {
//         setLoading(false);
//       }
//     };

//     fetchConfigs();
//   }, []);

//   const handleInputChange = (e) => {
//     const { name, value } = e.target;
//     setConfig((prevConfig) => ({
//       ...prevConfig,
//       [name]: value,
//     }));
//   };

//   const handleSaveConfig = async () => {
//     try {
//       const response = await fetch('/api/configs/save', {
//         method: 'POST',
//         headers: {
//           'Content-Type': 'application/json',
//         },
//         body: JSON.stringify(config),
//       });
//       const savedConfig = await response.json();
//       if (response.status === 201) {
//         setConfigs([...configs, savedConfig]);
//         setConfig({
//           numberOfVendors: '',
//           totalTickets: '',
//           ticketReleaseRate: '',
//           numberOfCustomers: '',
//           customerRetrievalRate: '',
//           maxTicketCapacity: '',
//           numberOfVIPCustomers: '',
//           ticketsPerVendor: '',
//           ticketsPerCustomer: '',
//         });
//       }
//     } catch (error) {
//       console.error('Error saving configuration:', error);
//     }
//   };

//   const handleDeleteConfig = async (id) => {
//     try {
//       const response = await fetch(`/api/configs/delete?id=${id}`, {
//         method: 'DELETE',
//       });
//       if (response.status === 204) {
//         setConfigs(configs.filter((config) => config.id !== id));
//       } else {
//         alert('Error deleting configuration');
//       }
//     } catch (error) {
//       console.error('Error deleting configuration:', error);
//     }
//   };

//   return (
//     <div>
//       <h1>Configuration Dashboard</h1>
//       <div>
//         <h2>Add New Configuration</h2>
//         <form
//           onSubmit={(e) => {
//             e.preventDefault();
//             handleSaveConfig();
//           }}
//         >
//           {/* Form Fields for Configuration */}
//           <div>
//             <label>Number of Vendors</label>
//             <input
//               type="number"
//               name="numberOfVendors"
//               value={config.numberOfVendors}
//               onChange={handleInputChange}
//             />
//           </div>
//           {/* Other form fields here... */}
//           <button type="submit">Save Configuration</button>
//         </form>
//       </div>

//       <div>
//         <h2>Existing Configurations</h2>
//         {loading ? (
//           <p>Loading...</p>
//         ) : (
//           <ul>
//             {configs.map((config) => (
//               <li key={config.id}>
//                 <div>
//                   <strong>Configuration ID: {config.id}</strong>
//                   <ul>
//                     {/* Configuration Details */}
//                     <li>Number of Vendors: {config.numberOfVendors}</li>
//                     {/* Other configuration details... */}
//                   </ul>
//                   <Link to={`/simulation/${config.id}`}>Start Simulation</Link>
//                   <button onClick={() => handleDeleteConfig(config.id)}>Delete</button>
//                 </div>
//               </li>
//             ))}
//           </ul>
//         )}
//       </div>
//     </div>
//   );
// };

// export default ConfigurationDashboard;
