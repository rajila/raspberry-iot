using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web.Http;
using System.Web.Http.Description;
using dispenserService.Models;
using dispenserService.Util;

namespace dispenserService.Controllers
{
    public class ConfigurationsController : ApiController
    {
        private dispenserServiceContext db = new dispenserServiceContext();

        // GET: api/Configurations
        public IQueryable<Configuration> GetConfigurations()
        {
            return db.Configurations;
        }

        [Route("api/Dispenser")]
        [HttpGet]
        public Dispenser GetDispenser()
        {
            DateTime _time = DateTime.Now;
            Dispenser _dispenser = new Dispenser();
            _dispenser.ListConfiguration = db.Configurations.ToList();
            _dispenser.ListFoodHour = db.FoodHours.ToList();
            _dispenser.ListFoodDispenser = db.FoodDispensers.Where(s => s.CurrentDateTime.Year == _time.Year &&
                                                   s.CurrentDateTime.Month == _time.Month &&
                                                   s.CurrentDateTime.Day == _time.Day).ToList();
            _dispenser.ListWaterDispenser = db.WaterDispensers.Where(s => s.CurrentDateTime.Year == _time.Year &&
                                                   s.CurrentDateTime.Month == _time.Month &&
                                                   s.CurrentDateTime.Day == _time.Day).ToList();
            _dispenser.ListLastLog = db.Logs.OrderByDescending(s => s.CurrentDateTime).Take(1).ToList();
            return _dispenser;
        }

        // GET: api/Configurations/5
        [ResponseType(typeof(Configuration))]
        public async Task<IHttpActionResult> GetConfiguration(int id)
        {
            Configuration configuration = await db.Configurations.FindAsync(id);
            if (configuration == null)
            {
                return NotFound();
            }

            return Ok(configuration);
        }

        // PUT: api/Configurations/5
        [ResponseType(typeof(void))]
        public async Task<IHttpActionResult> PutConfiguration(int id, Configuration configuration)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != configuration.ConfigurationId)
            {
                return BadRequest();
            }

            db.Entry(configuration).State = EntityState.Modified;

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!ConfigurationExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return StatusCode(HttpStatusCode.NoContent);
        }

        // POST: api/Configurations
        [ResponseType(typeof(Configuration))]
        public async Task<IHttpActionResult> PostConfiguration(Configuration configuration)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.Configurations.Add(configuration);
            await db.SaveChangesAsync();

            return CreatedAtRoute("DefaultApi", new { id = configuration.ConfigurationId }, configuration);
        }

        // DELETE: api/Configurations/5
        [ResponseType(typeof(Configuration))]
        public async Task<IHttpActionResult> DeleteConfiguration(int id)
        {
            Configuration configuration = await db.Configurations.FindAsync(id);
            if (configuration == null)
            {
                return NotFound();
            }

            db.Configurations.Remove(configuration);
            await db.SaveChangesAsync();

            return Ok(configuration);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool ConfigurationExists(int id)
        {
            return db.Configurations.Count(e => e.ConfigurationId == id) > 0;
        }
    }
}