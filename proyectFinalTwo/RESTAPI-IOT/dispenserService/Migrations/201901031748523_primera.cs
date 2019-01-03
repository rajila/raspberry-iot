namespace dispenserService.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class primera : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "dbo.Configurations",
                c => new
                    {
                        ConfigurationId = c.Int(nullable: false, identity: true),
                        AmountDailyFood = c.Double(nullable: false),
                        FoodPortion = c.Double(nullable: false),
                        LiterWater = c.Double(nullable: false),
                        MinPercentWater = c.Int(nullable: false),
                        CurrentDateTime = c.DateTime(nullable: false),
                    })
                .PrimaryKey(t => t.ConfigurationId);
            
            CreateTable(
                "dbo.FoodDispensers",
                c => new
                    {
                        FoodDispenserId = c.Int(nullable: false, identity: true),
                        ThingId = c.String(),
                        ActionType = c.String(),
                        AmountDailyFood = c.Double(nullable: false),
                        FoodPortion = c.Double(nullable: false),
                        CurrentAmountFood = c.Double(nullable: false),
                        AmountFoodDownloaded = c.Double(nullable: false),
                        AngleServoFood = c.Int(nullable: false),
                        OpeningSecondsFood = c.Int(nullable: false),
                        CurrentDate = c.DateTime(nullable: false),
                        CurrentTime = c.DateTime(nullable: false),
                    })
                .PrimaryKey(t => t.FoodDispenserId);
            
            CreateTable(
                "dbo.FoodHours",
                c => new
                    {
                        FoodHourId = c.Int(nullable: false, identity: true),
                        FoodHourType = c.String(),
                        Hour = c.DateTime(nullable: false),
                    })
                .PrimaryKey(t => t.FoodHourId);
            
            CreateTable(
                "dbo.Logs",
                c => new
                    {
                        LogId = c.Int(nullable: false, identity: true),
                        ThingId = c.String(),
                        ActionType = c.String(),
                        AmountDailyFood = c.Double(nullable: false),
                        FoodPortion = c.Double(nullable: false),
                        LiterWater = c.Double(nullable: false),
                        MinPercentWater = c.Int(nullable: false),
                        CurrentAmountFood = c.Double(nullable: false),
                        AmountFoodDownloaded = c.Double(nullable: false),
                        CurrentPercentWater = c.Int(nullable: false),
                        LiterWaterDownloaded = c.Double(nullable: false),
                        AngleServoFood = c.Int(nullable: false),
                        OpeningSecondsFood = c.Int(nullable: false),
                        AngleServoWater = c.Int(nullable: false),
                        OpeningSecondsWater = c.Int(nullable: false),
                        CurrentDate = c.DateTime(nullable: false),
                        CurrentTime = c.DateTime(nullable: false),
                    })
                .PrimaryKey(t => t.LogId);
            
            CreateTable(
                "dbo.WaterDispensers",
                c => new
                    {
                        WaterDispenserId = c.Int(nullable: false, identity: true),
                        ThingId = c.String(),
                        ActionType = c.String(),
                        LiterWater = c.Double(nullable: false),
                        MinPercentWater = c.Int(nullable: false),
                        CurrentPercentWater = c.Int(nullable: false),
                        LiterWaterDownloaded = c.Double(nullable: false),
                        AngleServoWater = c.Int(nullable: false),
                        OpeningSecondsWater = c.Int(nullable: false),
                        CurrentDate = c.DateTime(nullable: false),
                        CurrentTime = c.DateTime(nullable: false),
                    })
                .PrimaryKey(t => t.WaterDispenserId);
            
        }
        
        public override void Down()
        {
            DropTable("dbo.WaterDispensers");
            DropTable("dbo.Logs");
            DropTable("dbo.FoodHours");
            DropTable("dbo.FoodDispensers");
            DropTable("dbo.Configurations");
        }
    }
}
