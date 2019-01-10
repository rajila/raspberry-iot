namespace dispenserService.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class sexta : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "dbo.ConfigurationParameters",
                c => new
                    {
                        ConfigurationId = c.Int(nullable: false, identity: true),
                        AmountDailyFood = c.Double(nullable: false),
                        AmountBowlFoodWater = c.Double(nullable: false),
                        AmountDailyWater = c.Double(nullable: false),
                        CurrentDateTime = c.DateTime(nullable: false),
                    })
                .PrimaryKey(t => t.ConfigurationId);
            
            AddColumn("dbo.FoodDispensers", "AmountBowlFood", c => c.Double(nullable: false));
            AddColumn("dbo.Logs", "AmountBowlFood", c => c.Double(nullable: false));
            AddColumn("dbo.Logs", "AmountDailyWater", c => c.Double(nullable: false));
            AddColumn("dbo.Logs", "AmountBowlWater", c => c.Double(nullable: false));
            AddColumn("dbo.Logs", "CurrentAmountWater", c => c.Double(nullable: false));
            AddColumn("dbo.Logs", "AmountWaterDownloaded", c => c.Double(nullable: false));
            AddColumn("dbo.WaterDispensers", "AmountDailyWater", c => c.Double(nullable: false));
            AddColumn("dbo.WaterDispensers", "AmountBowlWater", c => c.Double(nullable: false));
            AddColumn("dbo.WaterDispensers", "CurrentAmountWater", c => c.Double(nullable: false));
            AddColumn("dbo.WaterDispensers", "AmountWaterDownloaded", c => c.Double(nullable: false));
            DropColumn("dbo.FoodDispensers", "FoodPortion");
            DropColumn("dbo.Logs", "FoodPortion");
            DropColumn("dbo.Logs", "MilliLiterWater");
            DropColumn("dbo.Logs", "MinPercentWater");
            DropColumn("dbo.Logs", "CurrentMilliLiterWater");
            DropColumn("dbo.Logs", "MilliLiterWaterDownloaded");
            DropColumn("dbo.WaterDispensers", "MilliLiterWater");
            DropColumn("dbo.WaterDispensers", "MinPercentWater");
            DropColumn("dbo.WaterDispensers", "CurrentMilliLiterWater");
            DropColumn("dbo.WaterDispensers", "MilliLiterWaterDownloaded");
            DropTable("dbo.Configurations");
            DropTable("dbo.FoodHours");
        }
        
        public override void Down()
        {
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
                "dbo.Configurations",
                c => new
                    {
                        ConfigurationId = c.Int(nullable: false, identity: true),
                        AmountDailyFood = c.Double(nullable: false),
                        AmoundBowlFoodWater = c.Double(nullable: false),
                        MilliLiterWater = c.Double(nullable: false),
                        CurrentDateTime = c.DateTime(nullable: false),
                    })
                .PrimaryKey(t => t.ConfigurationId);
            
            AddColumn("dbo.WaterDispensers", "MilliLiterWaterDownloaded", c => c.Double(nullable: false));
            AddColumn("dbo.WaterDispensers", "CurrentMilliLiterWater", c => c.Int(nullable: false));
            AddColumn("dbo.WaterDispensers", "MinPercentWater", c => c.Int(nullable: false));
            AddColumn("dbo.WaterDispensers", "MilliLiterWater", c => c.Double(nullable: false));
            AddColumn("dbo.Logs", "MilliLiterWaterDownloaded", c => c.Double(nullable: false));
            AddColumn("dbo.Logs", "CurrentMilliLiterWater", c => c.Int(nullable: false));
            AddColumn("dbo.Logs", "MinPercentWater", c => c.Int(nullable: false));
            AddColumn("dbo.Logs", "MilliLiterWater", c => c.Double(nullable: false));
            AddColumn("dbo.Logs", "FoodPortion", c => c.Double(nullable: false));
            AddColumn("dbo.FoodDispensers", "FoodPortion", c => c.Double(nullable: false));
            DropColumn("dbo.WaterDispensers", "AmountWaterDownloaded");
            DropColumn("dbo.WaterDispensers", "CurrentAmountWater");
            DropColumn("dbo.WaterDispensers", "AmountBowlWater");
            DropColumn("dbo.WaterDispensers", "AmountDailyWater");
            DropColumn("dbo.Logs", "AmountWaterDownloaded");
            DropColumn("dbo.Logs", "CurrentAmountWater");
            DropColumn("dbo.Logs", "AmountBowlWater");
            DropColumn("dbo.Logs", "AmountDailyWater");
            DropColumn("dbo.Logs", "AmountBowlFood");
            DropColumn("dbo.FoodDispensers", "AmountBowlFood");
            DropTable("dbo.ConfigurationParameters");
        }
    }
}
