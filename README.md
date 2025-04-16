# IS25-AM06

## Project: Galaxy Trucker

## Group Member:
  - Soheil Akharraze
  - Federico Bulfari
  - Ennio Cristianelli
  - Domenico De Giorgio

## Currently Uploaded:
  - GameModelUML
  - Cards FSM
  - Card abstract class and its subclasses: CombatZone, Planets, AbandonedShip, AbandonedStation, StarDust, OpenSpace, MeteorSwarm, Enemies subclasses (Pirates, Smugglers, Slavers), Epidemic.
  - Auxiliary classes needed to make the previous work (Projectiles and Subclasses, Penalty and subclasses, Planet)
  - CardDeck, CardCollection, TrialCardDeck and the json needed to make the CardDeck and the TrialFlightCardDeck
  - Tiles and its subclasses: VoidTile, CargoHold (RedCargo,BluCargo), Pipe, Engine(Single Engine, Double Engine), Cannon (Single Cannon, Double Cannon), Cabin (Starting Cabin, EquipCabin), BatteryComponents, 
    AlienSupportSystem, Shields.
  - Auxiliary Classes needed to make the previous work: Coordinates and Goods. 
  - TilesFactory and tiles.json needed to produce the tilesDeck and tilesStack
  - ShipBoard
  - Player 
  - FlightBoard
  - Game and the interface it implements: GameInterface
  - GameController and the interface it implements: GameObserver
  - Network: VirtualControllerRMI,VirtualViewRMI, VirtualInterface
  - Client: ClientController, GoodsManager, CoordInputManager
  - Tested: all model classes
