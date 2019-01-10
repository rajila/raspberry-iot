namespace dispenserService.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class cuarta : DbMigration
    {
        public override void Up()
        {
            AddColumn("dbo.Configurations", "AmoundPortionFood", c => c.Double(nullable: false));
        }
        
        public override void Down()
        {
            DropColumn("dbo.Configurations", "AmoundPortionFood");
        }
    }
}
