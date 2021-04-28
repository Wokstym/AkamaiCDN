import { ThroughputSection, PacketLossSection, RttSection } from './components';
import './App.css'

function App() {

    return (
        <main  className="main" >
            <ThroughputSection/>
            <PacketLossSection/>
            <RttSection/>
        </main>
  );
}
export default App;
